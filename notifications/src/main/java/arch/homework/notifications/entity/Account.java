package arch.homework.notifications.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Account {

    private Long id;

    private Long userId;

    private BigDecimal balance;
}
