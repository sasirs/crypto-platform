package com.crypto.analytics.engine;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RSIEngine {

    public double calculate(List<Double> prices) {

        int period = 14;

        if (prices.size() < period + 1)
            return Double.NaN;

        double gain = 0;
        double loss = 0;

        for (int i = prices.size() - period; i < prices.size(); i++) {
            double change = prices.get(i) - prices.get(i - 1);

            if (change > 0)
                gain += change;
            else
                loss += Math.abs(change);
        }

        double avgGain = gain / period;
        double avgLoss = loss / period;

        if (avgLoss == 0)
            return 100;

        double rs = avgGain / avgLoss;

        return 100 - (100 / (1 + rs));
    }
}