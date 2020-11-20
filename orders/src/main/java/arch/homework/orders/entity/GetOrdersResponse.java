package arch.homework.orders.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GetOrdersResponse extends Result {

    private List<OrderInfo> orders;

    public GetOrdersResponse(Integer code, String message) {
        super(code, message);
    }
}
