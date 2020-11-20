package arch.homework.orders.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Order {

    private Long id;

    @JsonIgnore
    private Long userId;

    @JsonProperty("billing_status")
    private String billingStatus;

    @JsonProperty("warehouse_status")
    private String warehouseStatus;

    @JsonProperty("delivery_status")
    private String deliveryStatus;

    @JsonProperty("order_status")
    private String orderStatus;

    private String reason;

}
