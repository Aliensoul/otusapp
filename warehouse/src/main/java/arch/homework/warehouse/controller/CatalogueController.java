package arch.homework.warehouse.controller;

import arch.homework.warehouse.entity.*;
import arch.homework.warehouse.service.WarehouseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class CatalogueController {

    public static final String WAREHOUSE_CATALOGUE = "/warehouse/catalogue";
    public static final String GET_ITEM_BY_ID = WAREHOUSE_CATALOGUE + "/get_item_by_id";
    public static final String GET_ITEM_BY_CATEGORY = WAREHOUSE_CATALOGUE + "/get_items_by_category";
    public static final String GET_ITEM_BY_NAME = WAREHOUSE_CATALOGUE + "/get_item_by_name";

    private final WarehouseService warehouseService;

    @GetMapping(GET_ITEM_BY_ID)
    public ItemResult getItemById(@RequestParam Long itemId){
        return warehouseService.getItemById(itemId);
    }

    @GetMapping(GET_ITEM_BY_CATEGORY)
    public ItemsResult getItemsByCategory(@RequestParam String category){
        return warehouseService.getItemsByCategory(category);
    }

    @GetMapping(GET_ITEM_BY_NAME)
    public ItemResult getItemByName(@RequestParam String itemName){
        return warehouseService.getItemByName(itemName);
    }
}
