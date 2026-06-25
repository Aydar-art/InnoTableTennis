package innopolis.tabletennis.kafka;

import innopolis.tabletennis.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "telegram.kafka", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class KafkaConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    private final AuthenticationService authenticationService;

    @Bean
    public NewTopic telegramTopic() {
        return TopicBuilder.name("telegramUserUpdate").build();
    }

    @KafkaListener(topics = {"telegramUserUpdate"}, id = "telegramAuthService")
    void listener(KafkaTelegramUserUpdate data) {
        log.info("New telegram user update in kafka: {}", data);
        authenticationService.saveOrUpdateUsername(data.getOldUser().getId(), data.getUpdatedUser().getUserName());
    }

    @Bean
    public ConsumerFactory<String, KafkaTelegramUserUpdate> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
            JsonDeserializer.TYPE_MAPPINGS, "com.system205.telegram.dto.TelegramUserUpdate:innopolis.tabletennis.kafka.KafkaTelegramUserUpdate",
            JsonDeserializer.TRUSTED_PACKAGES, "com.system205.*"
        ));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaTelegramUserUpdate> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaTelegramUserUpdate> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}

