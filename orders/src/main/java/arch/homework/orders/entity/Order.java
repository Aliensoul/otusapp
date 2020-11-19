package arch.homework.orders.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Order {

    private Long id;

    private Long userId;

    private String billingStatus;

    private String warehouseStatus;

    private String deliveryStatus;

    private String orderStatus;

    private String reason;

}
