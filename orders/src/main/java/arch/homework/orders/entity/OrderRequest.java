package arch.homework.orders.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Data
public class OrderRequest {

    @JsonProperty("order_id")
    private Long orderId;
}
