package com.crypto.notification.controller;


import com.crypto.notification.model.UserSubscription;
import com.crypto.notification.model.ChannelType;
import com.crypto.notification.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscription")
@RequiredArgsConstructor
public class UserSubscriptionController {

    private final UserSubscriptionRepository subscriptionRepository;

    @PostMapping
    public UserSubscription subscribe(@RequestBody UserSubscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @DeleteMapping("/{id}")
    public void unsubscribe(@PathVariable Long id) {
        subscriptionRepository.deleteById(id);
    }

    @GetMapping
    public List<UserSubscription> getAll() {
        return subscriptionRepository.findAll();
    }

    @GetMapping("/symbol/{symbol}")
    public List<UserSubscription> getBySymbol(@PathVariable String symbol) {
        return subscriptionRepository.findAll().stream()
                .filter(s -> s.getSymbol().equalsIgnoreCase(symbol))
                .toList();
    }
}