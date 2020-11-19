package arch.homework.billing.consumer;

import arch.homework.billing.entity.*;
import arch.homework.billing.mapper.AccountMapper;
import arch.homework.billing.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.List;
import java.util.Objects;

import static arch.homework.billing.config.kafka.KafkaConfig.KAFKA_CONSUMER;

@AllArgsConstructor
@Component
public class UsersConsumer {

    private final static String ACCOUNTS_TOPIC = "accounts";
    private final static String USERS_TOPIC = "users";

    private final static String USERS_CONSUMER_ID = "users.billing";

    private final AccountRepository accountRepository;
    private final AccountMapper mapper;
    private final KafkaTemplate<String, AccountEvent> kafkaTemplate;

    @KafkaListener(id = USERS_CONSUMER_ID, topics = USERS_TOPIC, groupId = USERS_CONSUMER_ID, containerFactory = KAFKA_CONSUMER)
    public void processUser(List<ConsumerRecord<String, UserEvent>> consumerRecordList, Acknowledgment acknowledgment) {

        consumerRecordList.stream()
                .map(ConsumerRecord::value)
                .filter(Objects::nonNull)
                .filter(event -> event.getEventType().equals(EventType.CREATE))
                .map(UserEvent::getUser)
                .filter(user -> !accountRepository.existsUsersAccount(user))
                .map(user -> mapper.toAccount(user, accountRepository.createAccount(user)))
                .forEach(account -> kafkaTemplate.send(ACCOUNTS_TOPIC, new AccountEvent().setAccount(account)
                        .setEventType(EventType.CREATE))
                        .completable().join().getRecordMetadata().hasOffset());

        acknowledgment.acknowledge();
    }
}
