package com.crypto.analytics.config;

import com.crypto.analytics.model.PriceEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServers;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PriceEvent> priceEventKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PriceEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();

        // Configure ObjectMapper with JavaTimeModule
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // Configure JsonDeserializer
        JsonDeserializer<PriceEvent> jsonDeserializer = new JsonDeserializer<>(PriceEvent.class, mapper);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.addTrustedPackages("*");
        jsonDeserializer.setUseTypeMapperForKey(true);

        // Wrap in ErrorHandlingDeserializer
        ErrorHandlingDeserializer<PriceEvent> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(
                jsonDeserializer);

        // Create Consumer Factory with explicit deserializers
        DefaultKafkaConsumerFactory<String, PriceEvent> consumerFactory = new DefaultKafkaConsumerFactory<>(
                consumerConfigs(), new StringDeserializer(), errorHandlingDeserializer);

        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(3);
        factory.setCommonErrorHandler(new DefaultErrorHandler(new FixedBackOff(1000L, 2)));

        return factory;
    }

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "analytics-service-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return props;
    }
}