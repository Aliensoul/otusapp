package arch.homework.warehouse.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetItemByCategoryRequest {

    @JsonProperty("item_category")
    private String itemCategory;
}
