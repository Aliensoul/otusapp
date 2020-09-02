package arch.homework.otusapp.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Result {

    private String message;

    public Result(String message) {
        this.message = message;
    }

    public Result() {
    }
}
