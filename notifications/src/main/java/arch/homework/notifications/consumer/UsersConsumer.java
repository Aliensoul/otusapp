package arch.homework.notifications.consumer;

import arch.homework.notifications.config.kafka.KafkaConfig;
import arch.homework.notifications.entity.EventType;
import arch.homework.notifications.entity.UserEvent;
import arch.homework.notifications.repository.impl.MailRepositoryImpl;
import arch.homework.notifications.service.MailService;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Component
public class UsersConsumer {

    private final static String USERS_TOPIC = "users";

    private final static String USERS_CONSUMER_ID = "users.notifications";

    private final MailService mailService;

    @KafkaListener(id = USERS_CONSUMER_ID, topics = USERS_TOPIC, groupId = USERS_CONSUMER_ID, containerFactory = KafkaConfig.KAFKA_CONSUMER)
    public void processUser(List<ConsumerRecord<String, UserEvent>> consumerRecordList, Acknowledgment acknowledgment) {

        consumerRecordList.stream()
                .map(ConsumerRecord::value)
                .filter(Objects::nonNull)
                .filter(event -> event.getEventType().equals(EventType.CREATE))
                .map(UserEvent::getUser)
                .filter(user -> !mailService.existsUsersAccount(user))
                .forEach(mailService::createUser);

        acknowledgment.acknowledge();
    }
}
