package arch.homework.billing.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Operation {

    private Long orderId;

    private Long accountId;

    private BigDecimal amount;

    private String status;
}
