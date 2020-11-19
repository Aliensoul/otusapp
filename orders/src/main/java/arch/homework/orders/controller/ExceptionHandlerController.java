package arch.homework.orders.controller;

import arch.homework.orders.entity.Result;
import arch.homework.orders.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result errors(UnauthorizedException e) {
        return new Result(-999,"Заказ принадлежит другому пользователю");
    }

}
