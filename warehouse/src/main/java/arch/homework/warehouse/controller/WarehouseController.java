package arch.homework.warehouse.controller;

import arch.homework.warehouse.entity.Item;
import arch.homework.warehouse.entity.RestockItemsRequest;
import arch.homework.warehouse.entity.Result;
import arch.homework.warehouse.service.WarehouseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class WarehouseController {

    public static final String WAREHOUSE = "/warehouse";
    public static final String ADD_NEW_ITEM = WAREHOUSE + "/add_new_item";
    public static final String RESTOCK_ITEMS = WAREHOUSE + "/restock_items";

    private final WarehouseService warehouseService;

    @PostMapping(ADD_NEW_ITEM)
    public Result addNewItem(@RequestBody Item item){
        return warehouseService.addNewItem(item);
    }

    @PostMapping(RESTOCK_ITEMS)
    public Result restockItems(RestockItemsRequest restockItemsRequest){
        return warehouseService.restockItems(restockItemsRequest);
    }
}
