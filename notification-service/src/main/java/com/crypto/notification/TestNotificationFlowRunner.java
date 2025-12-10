//package com.crypto.notification;
//
//
//import com.crypto.notification.model.AnalyticsEvent;
//import com.crypto.notification.model.ChannelType;
//import com.crypto.notification.model.UserSubscription;
//import com.crypto.notification.repository.UserSubscriptionRepository;
//import com.crypto.notification.services.SignalProcessor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//
//@Component
//@RequiredArgsConstructor
//public class TestNotificationFlowRunner implements CommandLineRunner {
//
//    private final UserSubscriptionRepository subscriptionRepository;
//    private final SignalProcessor signalProcessor;
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        // STEP 1: Create demo subscriptions if not exists
//        if (subscriptionRepository.findAll().isEmpty()) {
//            UserSubscription emailUser = new UserSubscription();
//            emailUser.setUserId(1L);
//            emailUser.setSymbol("BTC");
//            emailUser.setChannel(ChannelType.EMAIL);
//            emailUser.setEmail("sasirs272@gmail.com");
//            subscriptionRepository.save(emailUser);
////
////            UserSubscription wsUser = new UserSubscription();
////            wsUser.setUserId(2L);
////            wsUser.setSymbol("BTC");
////            wsUser.setChannel(ChannelType.WEBSOCKET);
////            subscriptionRepository.save(wsUser);
//
//            System.out.println("✅ Demo users created");
//        }
//
//        // STEP 2: Create a demo AnalyticsEvent
//        AnalyticsEvent demoEvent = AnalyticsEvent.builder()
//                .symbol("BTC")
//                .price(50000.0)
//                .signal("BUY") // or SELL
//                .timestamp(System.currentTimeMillis())
//                .indicators(new HashMap<>())
//                .build();
//
//        // STEP 3: Process the event
//        signalProcessor.process(demoEvent);
//
//        System.out.println("📩 Demo notification sent for " + demoEvent.getSymbol() + " with signal " + demoEvent.getSignal());
//    }
//}