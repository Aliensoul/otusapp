package arch.homework.warehouse.config.kafka;

import arch.homework.warehouse.entity.CreateOrderEvent;
import arch.homework.warehouse.entity.OrderStatusChangedEvent;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.BatchErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.SeekToCurrentBatchErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Map;

@Configuration
@AllArgsConstructor
public class KafkaConfig {

    public static final String KAFKA_CONSUMER_ORDERS_NEW = "kafkaConsumerOrdersNew";
    public static final String KAFKA_CONSUMER_ORDERS_STATUS_CHANGE = "kafkaConsumerOrderStatusChange";
    public static final String KAFKA_PRODUCER = "kafkaProducer";

    private final KafkaConsumerProperties kafkaConsumerProperties;

    @Bean
    @Primary
    public BatchErrorHandler seekToCurrentBatchErrorHandler() {
        SeekToCurrentBatchErrorHandler seekToCurrentBatchErrorHandler = new SeekToCurrentBatchErrorHandler();
        FixedBackOff fixedBackOff = new FixedBackOff(1000, 1);
        seekToCurrentBatchErrorHandler.setBackOff(fixedBackOff);
        return seekToCurrentBatchErrorHandler;
    }

    @Bean(KAFKA_PRODUCER)
    @Primary
    public <V> KafkaTemplate<String, V> StringJsonKafkaTemplate(KafkaProperties kafkaProperties) {

        Map<String, Object> props = kafkaProperties.buildProducerProperties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
    }

    @Bean(KAFKA_CONSUMER_ORDERS_NEW)
    public ConcurrentKafkaListenerContainerFactory<Long, CreateOrderEvent> kafkaFactoryNewOrders(KafkaProperties kafkaProperties, BatchErrorHandler batchErrorHandler) {

        Map<String, Object> consumerProperties = kafkaProperties.buildConsumerProperties();

        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        ConcurrentKafkaListenerContainerFactory<Long, CreateOrderEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();


        DefaultKafkaConsumerFactory<Long, CreateOrderEvent> defaultKafkaConsumerFactory = new DefaultKafkaConsumerFactory<>(consumerProperties);
        defaultKafkaConsumerFactory.setValueDeserializer(new JsonDeserializer<>(CreateOrderEvent.class, false));

        factory.setConsumerFactory(defaultKafkaConsumerFactory);
        factory.setAutoStartup(true);
        factory.setConcurrency(kafkaConsumerProperties.getConcurrency());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setAckOnError(false);
        factory.getContainerProperties().setSyncCommits(true);


        factory.setBatchErrorHandler(batchErrorHandler);
        factory.setBatchListener(true);

        return factory;
    }

    @Bean(KAFKA_CONSUMER_ORDERS_STATUS_CHANGE)
    public ConcurrentKafkaListenerContainerFactory<Long, OrderStatusChangedEvent> kafkaFactoryOrderStatusChange(KafkaProperties kafkaProperties, BatchErrorHandler batchErrorHandler) {

        Map<String, Object> consumerProperties = kafkaProperties.buildConsumerProperties();

        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        ConcurrentKafkaListenerContainerFactory<Long, OrderStatusChangedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();


        DefaultKafkaConsumerFactory<Long, OrderStatusChangedEvent> defaultKafkaConsumerFactory = new DefaultKafkaConsumerFactory<>(consumerProperties);
        defaultKafkaConsumerFactory.setValueDeserializer(new JsonDeserializer<>(OrderStatusChangedEvent.class, false));

        factory.setConsumerFactory(defaultKafkaConsumerFactory);
        factory.setAutoStartup(true);
        factory.setConcurrency(kafkaConsumerProperties.getConcurrency());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setAckOnError(false);
        factory.getContainerProperties().setSyncCommits(true);


        factory.setBatchErrorHandler(batchErrorHandler);
        factory.setBatchListener(true);

        return factory;
    }

}
