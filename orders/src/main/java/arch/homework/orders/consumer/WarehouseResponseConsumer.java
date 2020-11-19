package arch.homework.orders.consumer;

import arch.homework.orders.entity.OrderStatusChangedEvent;
import arch.homework.orders.service.OrdersService;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

import static arch.homework.orders.config.kafka.KafkaConfig.KAFKA_RESULT_CONSUMER;

@AllArgsConstructor
@Component
public class WarehouseResponseConsumer {

    private final static String ORDERS_WAREHOUSE_TOPIC = "orders.warehouse";

    private final static String ORDERS_CONSUMER_ID = "orders.warehouse";

    private final OrdersService ordersService;

    @KafkaListener(id = ORDERS_CONSUMER_ID, topics = ORDERS_WAREHOUSE_TOPIC, groupId = ORDERS_CONSUMER_ID, containerFactory = KAFKA_RESULT_CONSUMER)
    public void processUser(List<ConsumerRecord<Long, OrderStatusChangedEvent>> consumerRecordList, Acknowledgment acknowledgment) {

        consumerRecordList.stream()
                .map(ConsumerRecord::value)
                .forEach(ordersService::processWarehouseResponse);

        acknowledgment.acknowledge();
    }
}
