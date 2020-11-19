package arch.homework.warehouse.entity;

import lombok.Data;

@Data
public class OrderedItem {

    private Long orderId;

    private Long itemId;

    private Long quantity;
}
