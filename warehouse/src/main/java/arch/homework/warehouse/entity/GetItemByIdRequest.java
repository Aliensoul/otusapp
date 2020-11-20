package arch.homework.warehouse.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetItemByIdRequest {

    @JsonProperty("item_id")
    private Long itemId;
}
