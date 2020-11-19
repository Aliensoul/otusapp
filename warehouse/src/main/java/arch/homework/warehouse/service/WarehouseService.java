package arch.homework.warehouse.service;


import arch.homework.warehouse.entity.*;

public interface WarehouseService {

    Result addNewItem(Item item);

    Result restockItems(RestockItemsRequest restockItemsRequest);

    boolean orderItemsReserved(Long key);

    OrderStatusChangedEvent reserveItems(Long key, CreateOrderEvent value);

    OrderStatusChangedEvent cancel(OrderStatusChangedEvent value);

    OrderStatusChangedEvent confirm(OrderStatusChangedEvent event);
}
