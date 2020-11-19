package arch.homework.warehouse.consumer;


import arch.homework.warehouse.entity.OrderStatusChangedEvent;
import arch.homework.warehouse.service.WarehouseService;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

import static arch.homework.warehouse.config.kafka.KafkaConfig.KAFKA_CONSUMER_ORDERS_STATUS_CHANGE;

@AllArgsConstructor
@Component
public class ConfirmedOrdersConsumer {

    private static final String ORDERS_CANCELLED_TOPIC = "orders.confirmed";
    private final static String ORDERS_WAREHOUSE_TOPIC = "orders.warehouse";

    private final static String ORDERS_CONSUMER_ID = "orders.warehouse.confirm";

    private final WarehouseService orderProcessingService;
    private final KafkaTemplate<String, OrderStatusChangedEvent> kafkaTemplate;

    @KafkaListener(id = ORDERS_CONSUMER_ID, topics = ORDERS_CANCELLED_TOPIC, groupId = ORDERS_CONSUMER_ID, containerFactory = KAFKA_CONSUMER_ORDERS_STATUS_CHANGE)
    public void processUser(List<ConsumerRecord<Long, OrderStatusChangedEvent>> consumerRecordList, Acknowledgment acknowledgment) {

        consumerRecordList.stream()
                .map(x -> orderProcessingService.confirm(x.value()))
                .forEach(account -> kafkaTemplate.send(ORDERS_WAREHOUSE_TOPIC, account)
                        .completable().join().getRecordMetadata().hasOffset());

        acknowledgment.acknowledge();
    }
}
