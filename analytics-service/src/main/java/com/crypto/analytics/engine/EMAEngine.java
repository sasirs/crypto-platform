package com.crypto.analytics.engine;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EMAEngine {

    public double calculate(List<Double> prices, int period) {

        if (prices.size() < period)
            return Double.NaN;

        double multiplier = 2.0 / (period + 1);

        double ema = prices.get(prices.size() - period);

        for (int i = prices.size() - period + 1; i < prices.size(); i++) {
            ema = ((prices.get(i) - ema) * multiplier) + ema;
        }

        return ema;
    }
}