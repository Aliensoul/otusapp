package arch.homework.warehouse.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RestockItemsRequest {

    private Long itemId;

    private Long quantity;
}
