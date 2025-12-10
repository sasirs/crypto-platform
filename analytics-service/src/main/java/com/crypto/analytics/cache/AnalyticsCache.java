package com.crypto.analytics.cache;

import com.crypto.analytics.model.AnalyticsEvent;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AnalyticsCache {

    private final Map<String, AnalyticsEvent> latestEvent = new HashMap<>();
    private final Map<String, LinkedList<AnalyticsEvent>> history = new HashMap<>();

    private static final int HISTORY_LIMIT = 100; // store last 100 analytics updates

    public synchronized void store(AnalyticsEvent event) {
        String symbol = event.getSymbol();

        latestEvent.put(symbol, event);

        history.putIfAbsent(symbol, new LinkedList<>());
        LinkedList<AnalyticsEvent> list = history.get(symbol);

        list.addLast(event);

        if (list.size() > HISTORY_LIMIT) {
            list.removeFirst();
        }
    }

    public synchronized AnalyticsEvent getLatest(String symbol) {
        return latestEvent.get(symbol);
    }

    public synchronized List<AnalyticsEvent> getHistory(String symbol, int limit) {
        LinkedList<AnalyticsEvent> list = history.getOrDefault(symbol, new LinkedList<>());
        return list.subList(Math.max(0, list.size() - limit), list.size());
    }

    public synchronized Set<String> getSymbols() {
        return latestEvent.keySet();
    }
}