package arch.homework.authserver.controller;

import arch.homework.authserver.entity.Result;
import arch.homework.authserver.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result errors(UnauthorizedException e) {
        return new Result("Нельзя просматривать информацию других пользователей");
    }

}
