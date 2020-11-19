package arch.homework.warehouse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Item {

    @JsonIgnore
    private Long id;

    private String name;

    private String category;

    private Long quantity;

    private BigDecimal price;
}
