package arch.homework.billing.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class GetBalanceResponse extends Result {

    public BigDecimal balance;

    public GetBalanceResponse(Integer code, String message) {
        super(code, message);
    }
}
