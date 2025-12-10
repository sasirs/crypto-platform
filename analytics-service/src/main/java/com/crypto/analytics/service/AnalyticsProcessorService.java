package com.crypto.analytics.service;

import com.crypto.analytics.cache.AnalyticsCache;
import com.crypto.analytics.cache.PriceHistoryCache;
import com.crypto.analytics.engine.*;
import com.crypto.analytics.model.*;
import com.crypto.analytics.publisher.AnalyticsEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsProcessorService {

    private final PriceHistoryCache historyCache;
    private final AnalyticsCache analyticsCache; // Added for local storage

    private final SMAEngine smaEngine;
    private final EMAEngine emaEngine;
    private final RSIEngine rsiEngine;
    private final MACDEngine macdEngine;
    private final VolatilityEngine volatilityEngine;
    private final SignalEngine signalEngine;

    private final AnalyticsEventPublisher publisher;

    /**
     * Main entry point for processing each PriceEvent.
     */
    public void process(PriceEvent priceEvent) {

        String symbol = priceEvent.getSymbol();
        double price = priceEvent.getPrice();

        log.info("📊 Processing analytics for {} → ${}", symbol, price);

        // 1. Update rolling window history
        historyCache.addPrice(symbol, price);

        List<Double> history = historyCache.getHistory(symbol);

        if (history.size() < 30) {
            log.warn("Not enough historical data for {} → size={}", symbol, history.size());
            return; // Not enough data to compute MACD/RSI properly
        }

        // 2. Calculate technical indicators
        double sma20 = smaEngine.calculate(history, 20);
        double ema20 = emaEngine.calculate(history, 20);
        double rsi14 = rsiEngine.calculate(history);
        MACDValues macd = macdEngine.calculate(history);
        double volatility = volatilityEngine.calculate(history);

        // 3. Generate buy/sell/hold signal
        SignalType signal = signalEngine.evaluate(rsi14, macd);

        // 4. Build analytics event
        AnalyticsEvent event = AnalyticsEvent.builder()
                .symbol(symbol)
                .price(price)
                .timestamp(priceEvent.getTimestamp())
                .indicators(IndicatorSet.builder()
                        .sma20(sma20)
                        .ema20(ema20)
                        .rsi14(rsi14)
                        .macd(macd)
                        .volatility(volatility)
                        .build())
                .signal(signal)
                .build();

        // 5. Store in local cache for API
        analyticsCache.store(event);

        // 6. Publish event to Kafka topic
        publisher.publish(event);

        log.info("📤 Published Analytics Event for {} → Signal: {}", symbol, signal);
    }
}