package com.crypto.notification.services;


import com.crypto.notification.model.AnalyticsEvent;
import com.crypto.notification.model.UserSubscription;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationSender {

    private final JavaMailSender mailSender;

    public boolean send(UserSubscription user, AnalyticsEvent event) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Crypto Alert: " + event.getSymbol() + " " + event.getSignal());
            helper.setText("Signal: " + event.getSignal() +
                    "\nPrice: " + event.getPrice() +
                    "\nTime: " + event.getTimestamp());

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}