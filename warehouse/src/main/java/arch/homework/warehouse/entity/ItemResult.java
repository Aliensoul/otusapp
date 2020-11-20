package arch.homework.warehouse.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ItemResult extends Result {

    private Item item;

    public ItemResult(Integer code, String message) {
        super(code, message);
    }
}
