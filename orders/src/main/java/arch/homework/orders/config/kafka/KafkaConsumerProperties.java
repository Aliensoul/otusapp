package arch.homework.orders.config.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties("kafka.properties")
@Data
@Validated
public class KafkaConsumerProperties {

    private Integer concurrency = 1;
}
