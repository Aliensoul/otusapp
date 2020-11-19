package arch.homework.orders.controller;

import arch.homework.orders.entity.CreateOrderRequest;
import arch.homework.orders.entity.CreateOrderResult;
import arch.homework.orders.entity.OrderRequest;
import arch.homework.orders.entity.Result;
import arch.homework.orders.service.OrdersService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
public class OrdersController {

    public final static String ORDERS = "/orders";
    public final static String CREATE_ORDER = ORDERS + "/create_order";
    public final static String CONFIRM_ORDER = ORDERS + "/confirm_order";
    public final static String CANCEL_ORDER = ORDERS + "/cancel_order";

    private final OrdersService ordersService;

    @PostMapping(CREATE_ORDER)
    public CreateOrderResult createOrder(@RequestBody CreateOrderRequest request, HttpServletRequest httpServletRequest){
        return ordersService.createOrder(request, Long.valueOf(httpServletRequest.getHeader("X-User-Id")));
    }

    @PostMapping(CONFIRM_ORDER)
    public Result confirmOrder(@RequestBody OrderRequest request, HttpServletRequest httpServletRequest){
        return ordersService.confirmOrder(request, Long.valueOf(httpServletRequest.getHeader("X-User-Id")));
    }

    @PostMapping(CANCEL_ORDER)
    public Result cancelOrder(@RequestBody OrderRequest request, HttpServletRequest httpServletRequest){
        return ordersService.cancelOrder(request, Long.valueOf(httpServletRequest.getHeader("X-User-Id")));
    }
}
