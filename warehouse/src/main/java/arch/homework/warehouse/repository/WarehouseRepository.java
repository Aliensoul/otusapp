package arch.homework.warehouse.repository;

import arch.homework.warehouse.entity.Item;
import arch.homework.warehouse.entity.OrderWarehouseStatus;
import arch.homework.warehouse.entity.OrderedItem;
import arch.homework.warehouse.entity.RestockItemsRequest;

import java.util.List;

public interface WarehouseRepository {

    void addNewItem(Item item);

    void restockItems(RestockItemsRequest restockItemsRequest);

    boolean orderItemsReserved(Long orderId);

    Item getItemWithLock(Long id);

    void reserveItem(Long itemId, Long orderId, Long quantity);

    void decreaseItemQuantity(Long itemId, Long quantity);

    List<OrderedItem> getOrderedItems(Long orderId);

    void changeReservationStatus(Long orderId, String status);
}
