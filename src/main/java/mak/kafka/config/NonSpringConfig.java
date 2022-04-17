package mak.kafka.config;

import lombok.AllArgsConstructor;
import mak.kafka.model.properties.ConsumerProperties;
import mak.kafka.model.properties.ProducerProperties;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class NonSpringConfig {

    ConsumerProperties consumerProperties;
    ProducerProperties producerProperties;

    @Bean
    public Admin getAdminClient() {
        return Admin.create(consumerProperties.getConfigMap());
    }

    @Bean
    public KafkaConsumer getKafkaConsumer() {
        return new KafkaConsumer<>(consumerProperties.getConfigMap());
    }

    @Bean
    public KafkaProducer getKafkaProducer() {
        return new KafkaProducer<>(producerProperties.getConfigMap());
    }
}
