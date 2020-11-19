package arch.homework.orders.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateOrderResult extends Result{

    private Long orderId;

    public CreateOrderResult(Integer code, String message) {
        super(code, message);
    }
}
