package com.crypto.notification.services;

import com.crypto.notification.model.AnalyticsEvent;
import com.crypto.notification.model.UserSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketNotificationSender {

    private final SimpMessagingTemplate messagingTemplate;

    public boolean send(UserSubscription user, AnalyticsEvent event) {
        try {
            messagingTemplate.convertAndSend("/topic/crypto/" + user.getUserId(), event);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}