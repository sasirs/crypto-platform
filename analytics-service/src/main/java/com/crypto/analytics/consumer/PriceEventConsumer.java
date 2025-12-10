package com.crypto.analytics.consumer;

import com.crypto.analytics.model.PriceEvent;
import com.crypto.analytics.service.AnalyticsProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceEventConsumer {

    private final AnalyticsProcessorService processorService;

    @KafkaListener(topics = "${kafka.input.topic}", groupId = "analytics-service-group", containerFactory = "priceEventKafkaListenerFactory")
    public void onMessage(PriceEvent event) {

        if (event == null) {
            log.warn("Received NULL PriceEvent — skipping");
            return;
        }

        log.info("📥 Received Price Event: {} = ${}", event.getSymbol(), event.getPrice());

        processorService.process(event);
    }
}