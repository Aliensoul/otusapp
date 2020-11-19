package arch.homework.warehouse.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Data
public class CreateOrderEvent {

    @Size(min = 1)
    private List<ItemsInfo> items;

    private DeliveryInfo deliveryInfo;

    private BigDecimal price;

    private Long userId;

    @Data
    public static class DeliveryInfo {

        private LocalDate date;

        private String address;
    }

    @Data
    public static class ItemsInfo {

        @JsonProperty("item_id")
        private Long itemId;

        private Long quantity;
    }
}
