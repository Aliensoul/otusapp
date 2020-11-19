package arch.homework.billing.service;

import arch.homework.billing.entity.CreateOrderEvent;
import arch.homework.billing.entity.OrderStatusChangedEvent;

public interface OrderProcessingService {
    OrderStatusChangedEvent pay(Long orderId, CreateOrderEvent request);

    OrderStatusChangedEvent cancel(OrderStatusChangedEvent value);

    OrderStatusChangedEvent confirm(OrderStatusChangedEvent value);
}
