package arch.homework.warehouse.controller;

import arch.homework.warehouse.entity.*;
import arch.homework.warehouse.service.WarehouseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class WarehouseController {

    public static final String WAREHOUSE_ADMIN = "/warehouse/admin";
    public static final String ADD_NEW_ITEM = WAREHOUSE_ADMIN + "/add_new_item";
    public static final String RESTOCK_ITEMS = WAREHOUSE_ADMIN + "/restock_items";

    private final WarehouseService warehouseService;

    @PostMapping(ADD_NEW_ITEM)
    public Result addNewItem(@RequestBody AddItem item){
        return warehouseService.addNewItem(item);
    }

    @PostMapping(RESTOCK_ITEMS)
    public Result restockItems(@RequestBody RestockItemsRequest restockItemsRequest) {
        return warehouseService.restockItems(restockItemsRequest);
    }
}
