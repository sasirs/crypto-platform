package com.crypto.analytics.engine;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VolatilityEngine {

    public double calculate(List<Double> prices) {

        if (prices.size() < 2)
            return Double.NaN;

        double mean = prices.stream().mapToDouble(v -> v).average().orElse(0);

        double variance = prices.stream()
                .mapToDouble(v -> Math.pow(v - mean, 2))
                .average()
                .orElse(0);

        return Math.sqrt(variance);
    }
}