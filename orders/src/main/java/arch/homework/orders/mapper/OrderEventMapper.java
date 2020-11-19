package arch.homework.orders.mapper;

import arch.homework.orders.entity.CreateOrderEvent;
import arch.homework.orders.entity.CreateOrderRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderEventMapper {

    CreateOrderEvent mapToEvent(CreateOrderRequest request, Long userId);
}
