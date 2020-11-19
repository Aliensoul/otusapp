package arch.homework.warehouse.consumer;

import arch.homework.warehouse.entity.CreateOrderEvent;
import arch.homework.warehouse.entity.OrderStatusChangedEvent;
import arch.homework.warehouse.service.WarehouseService;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

import static arch.homework.warehouse.config.kafka.KafkaConfig.KAFKA_CONSUMER_ORDERS_NEW;

@AllArgsConstructor
@Component
public class OrdersCreatedConsumer {

    private final static String ORDERS_TOPIC = "orders.new";
    private final static String ORDERS_WAREHOUSE_TOPIC = "orders.warehouse";

    private final static String ORDERS_CONSUMER_ID = "orders.warehouse";

    private final KafkaTemplate<String, OrderStatusChangedEvent> kafkaTemplate;

    private final WarehouseService warehouseService;

    @KafkaListener(id = ORDERS_CONSUMER_ID, topics = ORDERS_TOPIC, groupId = ORDERS_CONSUMER_ID, containerFactory = KAFKA_CONSUMER_ORDERS_NEW)
    public void processUser(List<ConsumerRecord<Long, CreateOrderEvent>> consumerRecordList, Acknowledgment acknowledgment) {

        consumerRecordList.stream()
                .filter(x -> !warehouseService.orderItemsReserved(x.key()))
                .map(x -> warehouseService.reserveItems(x.key(), x.value()))
                .forEach(account -> kafkaTemplate.send(ORDERS_WAREHOUSE_TOPIC, account)
                        .completable().join().getRecordMetadata().hasOffset());

        acknowledgment.acknowledge();
    }
}
