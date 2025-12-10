package com.crypto.notification.services;


import com.crypto.notification.model.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SignalProcessor processor;

    @KafkaListener(topics = "crypto-analytics-events", groupId = "notification-service-group")
    public void consumeAnalyticsEvent(AnalyticsEvent event) {
        System.out.println("📩 Received Analytics Event: " + event.getSymbol() + " " + event.getSignal());
        processor.process(event);
    }
}