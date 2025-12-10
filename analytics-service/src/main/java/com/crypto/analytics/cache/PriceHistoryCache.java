package com.crypto.analytics.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class PriceHistoryCache {

    private static final int WINDOW_SIZE = 100; // 100 historical prices (~8 minutes if 5-second interval)

    private final Map<String, Deque<Double>> priceMap = new HashMap<>();

    public synchronized void addPrice(String symbol, double price) {

        priceMap.putIfAbsent(symbol, new ArrayDeque<>());

        Deque<Double> history = priceMap.get(symbol);
        history.addLast(price);

        if (history.size() > WINDOW_SIZE) {
            history.removeFirst();
        }

        log.debug("Updated price window for {} → size={}", symbol, history.size());
    }

    public synchronized List<Double> getHistory(String symbol) {
        return new ArrayList<>(priceMap.getOrDefault(symbol, new ArrayDeque<>()));
    }

    public synchronized boolean hasEnoughData(String symbol, int minSize) {
        return priceMap.containsKey(symbol) && priceMap.get(symbol).size() >= minSize;
    }
}