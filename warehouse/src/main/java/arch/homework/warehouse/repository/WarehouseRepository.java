package arch.homework.warehouse.repository;

import arch.homework.warehouse.entity.*;

import java.util.List;

public interface WarehouseRepository {

    void addNewItem(AddItem item);

    void restockItems(RestockItemsRequest restockItemsRequest);

    boolean orderItemsReserved(Long orderId);

    Item getItemWithLock(Long id);

    void reserveItem(Long itemId, Long orderId, Long quantity);

    void decreaseItemQuantity(Long itemId, Long quantity);

    List<OrderedItem> getOrderedItems(Long orderId);

    void changeReservationStatus(Long orderId, String status);

    Item getItemById(Long itemId);

    List<Item> getItemsByCategory(String itemCategory);

    Item getItemsByName(String itemName);
}
