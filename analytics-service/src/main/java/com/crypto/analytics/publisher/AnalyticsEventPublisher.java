package com.crypto.analytics.publisher;

import com.crypto.analytics.model.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventPublisher {

    private final KafkaTemplate<String, AnalyticsEvent> kafkaTemplate;

    @Value("${kafka.output.topic}")
    private String outputTopic;

    public void publish(AnalyticsEvent event) {
        kafkaTemplate.send(outputTopic, event.getSymbol(), event);
        log.debug("Kafka Publish → topic={} symbol={}", outputTopic, event.getSymbol());
    }
}