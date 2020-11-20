package arch.homework.warehouse.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetItemByNameRequest {

    @JsonProperty("item_name")
    private String itemName;
}
