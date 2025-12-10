package com.crypto.analytics.config;

import com.crypto.analytics.model.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServers;

    @Bean
    public ProducerFactory<String, AnalyticsEvent> analyticsProducerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                org.springframework.kafka.support.serializer.JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, AnalyticsEvent> kafkaTemplate() {
        return new KafkaTemplate<>(analyticsProducerFactory());
    }
}