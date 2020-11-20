package arch.homework.orders.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderStatusResponse extends Result{

    private Order order;

    public OrderStatusResponse(Integer code, String message) {
        super(code, message);
    }
}
