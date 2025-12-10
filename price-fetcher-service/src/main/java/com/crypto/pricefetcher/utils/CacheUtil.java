package com.crypto.pricefetcher.utils;

import java.util.HashMap;
import java.util.Map;

public class CacheUtil {

    private final Map<String, Double> cache = new HashMap<>();

    public boolean isDuplicate(String coin, double price) {
        return cache.containsKey(coin) && cache.get(coin) == price;
    }

    public void update(String coin, double price) {
        cache.put(coin, price);
    }
}
