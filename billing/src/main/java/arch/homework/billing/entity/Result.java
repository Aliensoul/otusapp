package arch.homework.billing.entity;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@EqualsAndHashCode
@Accessors(chain = true)
@NoArgsConstructor
public class Result {

    private Integer code;

    private String message;

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
