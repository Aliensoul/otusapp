package arch.homework.billing.consumer;

import arch.homework.billing.entity.CreateOrderEvent;
import arch.homework.billing.entity.OrderStatusChangedEvent;
import arch.homework.billing.repository.OperationsRepository;
import arch.homework.billing.service.OrderProcessingService;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

import static arch.homework.billing.config.kafka.KafkaConfig.KAFKA_CONSUMER_ORDERS_NEW;

@AllArgsConstructor
@Component
public class OrdersCreatedConsumer {

    private final static String ORDERS_TOPIC = "orders.new";
    private final static String ORDERS_BILLING_TOPIC = "orders.billing";

    private final static String ORDERS_CONSUMER_ID = "orders.billing";

    private final OperationsRepository operationsRepository;
    private final OrderProcessingService orderProcessingService;
    private final KafkaTemplate<String, OrderStatusChangedEvent> kafkaTemplate;

    @KafkaListener(id = ORDERS_CONSUMER_ID, topics = ORDERS_TOPIC, groupId = ORDERS_CONSUMER_ID, containerFactory = KAFKA_CONSUMER_ORDERS_NEW)
    public void processUser(List<ConsumerRecord<Long, CreateOrderEvent>> consumerRecordList, Acknowledgment acknowledgment) {

        consumerRecordList.stream()
                .filter(x -> !operationsRepository.orderBilled(x.key()))
                .map(x -> orderProcessingService.pay(x.key(), x.value()))
                .forEach(account -> kafkaTemplate.send(ORDERS_BILLING_TOPIC, account)
                        .completable().join().getRecordMetadata().hasOffset());

        acknowledgment.acknowledge();
    }
}
