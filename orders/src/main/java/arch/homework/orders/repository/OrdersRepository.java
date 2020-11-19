package arch.homework.orders.repository;

import arch.homework.orders.entity.CreateOrderRequest;
import arch.homework.orders.entity.Order;
import arch.homework.orders.entity.SagaStatus;

public interface OrdersRepository {

    Long createOrder(CreateOrderRequest request, Long userId);

    void saveOrderDetails(CreateOrderRequest request, Long orderId);

    void saveOrderItems(CreateOrderRequest request, Long orderId);

    Order getOrder(Long orderId);

    Order getOrderWithLock(Long orderId);

    void updateBillingStatus(Long id, SagaStatus reserved);

    void failOrder(Long id, String message);

    void updateOrderStatus(Long id, SagaStatus prepared);

    void updateWarehouseStatus(Long id, SagaStatus prepared);
}
