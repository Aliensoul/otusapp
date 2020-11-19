package arch.homework.authserver.exception;

import lombok.Getter;

@Getter
public class BusinessLogicException extends RuntimeException {

    private int code;

    private String message;

    public BusinessLogicException(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
