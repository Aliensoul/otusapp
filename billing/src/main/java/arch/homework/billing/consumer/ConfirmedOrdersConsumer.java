package arch.homework.billing.consumer;

import arch.homework.billing.entity.OrderStatusChangedEvent;
import arch.homework.billing.service.OrderProcessingService;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

import static arch.homework.billing.config.kafka.KafkaConfig.KAFKA_CONSUMER_ORDERS_STATUS_CHANGE;

@AllArgsConstructor
@Component
public class ConfirmedOrdersConsumer {

    private static final String ORDERS_CONFIRMED_TOPIC = "orders.confirmed";
    private final static String ORDERS_BILLING_TOPIC = "orders.billing";

    private final static String ORDERS_CONSUMER_ID = "orders.billing.confirm";

    private final OrderProcessingService orderProcessingService;
    private final KafkaTemplate<String, OrderStatusChangedEvent> kafkaTemplate;

    @KafkaListener(id = ORDERS_CONSUMER_ID, topics = ORDERS_CONFIRMED_TOPIC, groupId = ORDERS_CONSUMER_ID, containerFactory = KAFKA_CONSUMER_ORDERS_STATUS_CHANGE)
    public void processUser(List<ConsumerRecord<Long, OrderStatusChangedEvent>> consumerRecordList, Acknowledgment acknowledgment) {

        consumerRecordList.stream()
                .map(x -> orderProcessingService.confirm(x.value()))
                .forEach(account -> kafkaTemplate.send(ORDERS_BILLING_TOPIC, account)
                        .completable().join().getRecordMetadata().hasOffset());

        acknowledgment.acknowledge();
    }
}
