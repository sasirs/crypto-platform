package com.crypto.pricefetcher.service.publisher;

import com.crypto.pricefetcher.model.CryptoPriceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service responsible for publishing standardized crypto price events to Kafka.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaPricePublisher {

    private final KafkaTemplate<String, CryptoPriceEvent> kafkaTemplate;

    @Value("${crypto.kafka.topic}")
    private String topic;

    /**
     * Publishes a price event to the configured Kafka topic.
     * Use the coin symbol as the Kafka key to ensure ordering per coin.
     *
     * @param event The normalized price event to publish.
     */
    public void publish(CryptoPriceEvent event) {
        try {
            kafkaTemplate.send(topic, event.getSymbol(), event)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Published event: {}", event);
                        } else {
                            log.error("Failed to publish event for {}: {}", event.getSymbol(), ex.getMessage());
                        }
                    });
        } catch (Exception e) {
            log.error("Exception during Kafka publish: {}", e.getMessage(), e);
        }
    }
}
