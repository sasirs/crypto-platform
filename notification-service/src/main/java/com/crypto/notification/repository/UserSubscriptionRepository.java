package com.crypto.notification.repository;


import com.crypto.notification.model.UserSubscription;
import com.crypto.notification.model.ChannelType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    List<UserSubscription> findBySymbolAndChannel(String symbol, ChannelType channel);
}