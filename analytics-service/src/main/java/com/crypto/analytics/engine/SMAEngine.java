package com.crypto.analytics.engine;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SMAEngine {

    public double calculate(List<Double> prices, int period) {
        if (prices.size() < period)
            return Double.NaN;

        List<Double> window = prices.subList(prices.size() - period, prices.size());

        double sum = 0;
        for (double p : window)
            sum += p;

        return sum / period;
    }
}