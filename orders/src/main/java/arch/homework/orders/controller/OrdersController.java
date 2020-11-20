package arch.homework.orders.controller;

import arch.homework.orders.entity.*;
import arch.homework.orders.service.OrdersService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
public class OrdersController {

    public final static String ORDERS = "/orders";
    public final static String CREATE_ORDER = ORDERS + "/create_order";
    public final static String CONFIRM_ORDER = ORDERS + "/confirm_order";
    public final static String CANCEL_ORDER = ORDERS + "/cancel_order";
    public final static String GET_ORDERS = ORDERS + "/get_orders";
    public final static String GET_ORDER_STATUS = ORDERS + "/get_order_status";

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

    @GetMapping(GET_ORDERS)
    public GetOrdersResponse getOrders(HttpServletRequest httpServletRequest){
        return ordersService.getOrders(Long.valueOf(httpServletRequest.getHeader("X-User-Id")));
    }

    @GetMapping(GET_ORDER_STATUS)
    public OrderStatusResponse getOrderStatus(@RequestParam Long orderId, HttpServletRequest httpServletRequest){
        return ordersService.getOrderStatus(orderId, Long.valueOf(httpServletRequest.getHeader("X-User-Id")));
    }
}
