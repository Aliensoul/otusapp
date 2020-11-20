package arch.homework.orders.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OrderDetails {

    private Long orderId;

    private String address;

    private LocalDate date;

    private BigDecimal price;
}
