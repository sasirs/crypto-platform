package com.crypto.analytics.service;

import com.crypto.analytics.cache.AnalyticsCache;
import com.crypto.analytics.model.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AnalyticsQueryService {

    private final AnalyticsCache analyticsCache;

    public AnalyticsEvent getLatest(String symbol) {
        return analyticsCache.getLatest(symbol.toUpperCase());
    }

    public List<AnalyticsEvent> getHistory(String symbol, int limit) {
        return analyticsCache.getHistory(symbol.toUpperCase(), limit);
    }

    public Set<String> getTrackedSymbols() {
        return analyticsCache.getSymbols();
    }
}