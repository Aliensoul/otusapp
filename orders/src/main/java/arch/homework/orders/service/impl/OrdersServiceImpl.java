package arch.homework.orders.service.impl;

import arch.homework.orders.entity.*;
import arch.homework.orders.exception.UnauthorizedException;
import arch.homework.orders.mapper.OrderEventMapper;
import arch.homework.orders.repository.OrdersRepository;
import arch.homework.orders.service.OrdersService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
@Transactional
public class OrdersServiceImpl implements OrdersService {

    private static final String ORDERS_NEW_TOPIC = "orders.new";
    private static final String ORDERS_CONFIRMED_TOPIC = "orders.confirmed";
    private static final String ORDERS_PREPARED_TOPIC = "orders.prepared";
    private static final String ORDERS_CANCELLED_TOPIC = "orders.cancelled";

    private final OrdersRepository ordersRepository;
    private final OrderEventMapper orderEventMapper;

    private final KafkaTemplate<Long, CreateOrderEvent> kafkaTemplate;
    private final KafkaTemplate<Long, OrderStatusChangedEvent> kafkaTemplateOrderStatusChanged;
    private final KafkaTemplate<String, Order> kafkaTemplateOrderPrepared;

    @Override
    @Transactional
    public CreateOrderResult createOrder(CreateOrderRequest request, Long userId) {
        Long orderId = ordersRepository.createOrder(request, userId);
        ordersRepository.saveOrderDetails(request, orderId);
        ordersRepository.saveOrderItems(request, orderId);

        kafkaTemplate.send(ORDERS_NEW_TOPIC, orderId, orderEventMapper.mapToEvent(request, userId));

        return new CreateOrderResult(0, "ok").setOrderId(orderId);
    }

    @Transactional
    @Override
    public void processBillingResponse(OrderStatusChangedEvent result) {
        Order order = ordersRepository.getOrderWithLock(result.getOrderId());

        if (result.getCode() != 0) {
            ordersRepository.updateBillingStatus(order.getId(), SagaStatus.FAILED);
            failOrder(order.getId(), result.getMessage(), order.getUserId());
            return;
        }

        if (!SagaStatus.FAILED.toString().equals(order.getWarehouseStatus())
                || !SagaStatus.CANCELED.toString().equals(order.getWarehouseStatus())) {
            ordersRepository.updateBillingStatus(order.getId(), SagaStatus.valueOf(result.getStatus()));
            order.setBillingStatus(result.getStatus());
        }

        postProcessResponse(order);
    }

    @Override
    @Transactional
    public void processWarehouseResponse(OrderStatusChangedEvent result) {
        Order order = ordersRepository.getOrderWithLock(result.getOrderId());

        if (result.getCode() != 0) {
            ordersRepository.updateWarehouseStatus(order.getId(), SagaStatus.FAILED);
            failOrder(order.getId(), result.getMessage(), order.getUserId());
            return;
        }

        if (!SagaStatus.FAILED.toString().equals(order.getWarehouseStatus())
                || !SagaStatus.CANCELED.toString().equals(order.getWarehouseStatus())) {
            ordersRepository.updateWarehouseStatus(order.getId(), SagaStatus.valueOf(result.getStatus()));
            order.setWarehouseStatus(result.getStatus());
        }

        postProcessResponse(order);
    }

    @Override
    public void postProcessResponse(Order order) {
        if (SagaStatus.PREPARED.toString().equals(order.getBillingStatus()) &&
                SagaStatus.PREPARED.toString().equals(order.getDeliveryStatus()) &&
                SagaStatus.PREPARED.toString().equals(order.getWarehouseStatus())) {
            ordersRepository.updateOrderStatus(order.getId(), SagaStatus.PREPARED);
            kafkaTemplateOrderPrepared.send(ORDERS_PREPARED_TOPIC, order);
        } else if(SagaStatus.COMPLETED.toString().equals(order.getBillingStatus()) &&
                SagaStatus.COMPLETED.toString().equals(order.getDeliveryStatus()) &&
                SagaStatus.COMPLETED.toString().equals(order.getWarehouseStatus())){
            ordersRepository.updateOrderStatus(order.getId(), SagaStatus.COMPLETED);
        }
    }

    @Override
    public void failOrder(Long orderId, String reason, Long userId) {
        ordersRepository.failOrder(orderId, reason);
        kafkaTemplateOrderStatusChanged.send(ORDERS_CANCELLED_TOPIC, new OrderStatusChangedEvent(0, "ok")
                .setStatus("failed")
                .setOrderId(orderId)
                .setUserId(userId));
    }

    @Override
    @Transactional
    public Result confirmOrder(OrderRequest request, Long userId) {
        Order order = ordersRepository.getOrderWithLock(request.getOrderId());
        if (!order.getUserId().equals(userId)) {
            throw new UnauthorizedException();
        }

        if (!order.getOrderStatus().equals(SagaStatus.PREPARED.toString())) {
            return new Result(-600, "Can only confirm prepared orders");
        }

        ordersRepository.updateOrderStatus(request.getOrderId(), SagaStatus.CONFIRMED);
        ordersRepository.updateWarehouseStatus(request.getOrderId(), SagaStatus.CONFIRMED);
        ordersRepository.updateBillingStatus(request.getOrderId(), SagaStatus.CONFIRMED);
        kafkaTemplateOrderStatusChanged.send(ORDERS_CONFIRMED_TOPIC, new OrderStatusChangedEvent(0, "ok")
                .setOrderId(order.getId()).setStatus(SagaStatus.CONFIRMED.toString())
                .setUserId(order.getUserId()));

        return new Result(0, "ok");
    }

    @Override
    public Result cancelOrder(OrderRequest request, Long userId) {
        Order order = ordersRepository.getOrderWithLock(request.getOrderId());

        if (!order.getUserId().equals(userId)) {
            throw new UnauthorizedException();
        }

        if (!(order.getOrderStatus().equals(SagaStatus.PREPARED.toString())
                || order.getOrderStatus().equals(SagaStatus.NEW.toString()))) {
            return new Result(-601, "Can cancel only new and prepared orders");
        }

        ordersRepository.updateOrderStatus(request.getOrderId(), SagaStatus.CANCELED);
        ordersRepository.updateWarehouseStatus(request.getOrderId(), SagaStatus.CANCELED);
        ordersRepository.updateBillingStatus(request.getOrderId(), SagaStatus.CANCELED);
        kafkaTemplateOrderStatusChanged.send(ORDERS_CANCELLED_TOPIC, new OrderStatusChangedEvent(0, "ok")
                .setOrderId(order.getId()).setStatus(SagaStatus.FAILED.toString())
                .setUserId(order.getUserId()));

        return new Result(0, "ok");
    }
}
