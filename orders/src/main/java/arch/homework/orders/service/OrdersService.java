package arch.homework.orders.service;

import arch.homework.orders.entity.*;

public interface OrdersService {
    CreateOrderResult createOrder(CreateOrderRequest request, Long userId);

    void processBillingResponse(OrderStatusChangedEvent result);

    void processWarehouseResponse(OrderStatusChangedEvent result);

    void postProcessResponse(Order order);

    void failOrder(Long orderId, String reason, Long userId);

    Result confirmOrder(OrderRequest request, Long userId);

    Result cancelOrder(OrderRequest request, Long valueOf);

    GetOrdersResponse getOrders(Long userId);

    OrderStatusResponse getOrderStatus(Long orderId, Long valueOf);
}
