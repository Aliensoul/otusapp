package arch.homework.billing.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
public class OrderStatusChangedEvent extends Result{

    private Long orderId;

    private Long userId;

    private String status;

    public OrderStatusChangedEvent(Integer code, String message) {
        super(code, message);
    }
}
