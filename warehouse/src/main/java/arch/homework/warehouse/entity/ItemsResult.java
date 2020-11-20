package arch.homework.warehouse.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ItemsResult extends Result {

    private List<Item> items;

    public ItemsResult(Integer code, String message) {
        super(code, message);
    }
}
