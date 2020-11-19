package arch.homework.billing.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class MakeReplenishmentRequest {

    @Min(value = 0)
    private BigDecimal amount;

}
