package com.crypto.notification.services;


import com.crypto.notification.model.AnalyticsEvent;
import com.crypto.notification.model.ChannelType;
import com.crypto.notification.model.Notification;
import com.crypto.notification.model.UserSubscription;
import com.crypto.notification.repository.NotificationRepository;
import com.crypto.notification.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SignalProcessor {

    private final UserSubscriptionRepository subscriptionRepository;
    private final NotificationRepository notificationRepository;
    private final EmailNotificationSender emailSender;
    private final WebSocketNotificationSender webSocketSender;

    @Transactional
    public void process(AnalyticsEvent event) {
        if (!event.getSignal().equalsIgnoreCase("BUY") && !event.getSignal().equalsIgnoreCase("SELL")) {
            return; // Ignore HOLD
        }

        for (ChannelType channel : ChannelType.values()) {
            List<UserSubscription> users = subscriptionRepository.findBySymbolAndChannel(event.getSymbol(), channel);

            for (UserSubscription user : users) {
                boolean sent = false;

                if (channel == ChannelType.EMAIL) sent = emailSender.send(user, event);
                if (channel == ChannelType.WEBSOCKET) sent = webSocketSender.send(user, event);

                Notification notification = new Notification();
                notification.setUserId(user.getUserId());
                notification.setSymbol(event.getSymbol());
                notification.setSignal(event.getSignal());
                notification.setPrice(event.getPrice());
                notification.setChannel(channel);
                notification.setStatus(sent ? "SENT" : "FAILED");
                notification.setTimestamp(System.currentTimeMillis());

                notificationRepository.save(notification);
            }
        }
    }
}