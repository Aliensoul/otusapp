package arch.homework.warehouse.service.impl;

import arch.homework.warehouse.entity.*;
import arch.homework.warehouse.repository.WarehouseRepository;
import arch.homework.warehouse.service.WarehouseService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Component
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Override
    public Result addNewItem(Item item) {
        try {
            warehouseRepository.addNewItem(item);
        } catch (DuplicateKeyException e) {
            return new Result(-200, "Такой прдемет уже существует");
        }
        return new Result(0, "ok");
    }

    @Override
    public Result restockItems(RestockItemsRequest restockItemsRequest) {
        warehouseRepository.restockItems(restockItemsRequest);
        return new Result(0, "ok");
    }

    @Override
    public boolean orderItemsReserved(Long key) {
        return warehouseRepository.orderItemsReserved(key);
    }

    @Override
    @Transactional
    public OrderStatusChangedEvent reserveItems(Long orderId, CreateOrderEvent orderEvent) {
        for (CreateOrderEvent.ItemsInfo item : orderEvent.getItems()) {
            Item it = warehouseRepository.getItemWithLock(item.getItemId());

            if (it.getQuantity().compareTo(item.getQuantity()) == -1) {
                return new OrderStatusChangedEvent(-600, "Warehouse have less items then ordered").setOrderId(orderId);
            }

            warehouseRepository.reserveItem(item.getItemId(), orderId, item.getQuantity());
            warehouseRepository.decreaseItemQuantity(item.getItemId(), item.getQuantity());
        }

        return new OrderStatusChangedEvent(0, "ok").setOrderId(orderId)
                .setStatus(SagaStatus.PREPARED.toString());
    }

    @Override
    @Transactional
    public OrderStatusChangedEvent cancel(OrderStatusChangedEvent value) {
        List<OrderedItem> items = warehouseRepository.getOrderedItems(value.getOrderId());
        items.forEach(x -> warehouseRepository.changeReservationStatus(x.getOrderId(), OrderWarehouseStatus.CANCELLED.toString()));
        items.forEach(x -> warehouseRepository.restockItems(new RestockItemsRequest()
                .setItemId(x.getItemId()).setQuantity(x.getQuantity())));

        return value.setStatus(SagaStatus.CANCELED.toString());
    }

    @Override
    @Transactional
    public OrderStatusChangedEvent confirm(OrderStatusChangedEvent event) {
        List<OrderedItem> items = warehouseRepository.getOrderedItems(event.getOrderId());
        items.forEach(x -> warehouseRepository.changeReservationStatus(x.getOrderId(), OrderWarehouseStatus.SHIPPED.toString()));

        return event.setStatus(SagaStatus.COMPLETED.toString());
    }
}
