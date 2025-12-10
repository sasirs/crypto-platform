package com.crypto.analytics.engine;

import com.crypto.analytics.model.MACDValues;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MACDEngine {

    private final EMAEngine ema;

    public MACDValues calculate(List<Double> prices) {

        if (prices.size() < 26) {
            return MACDValues.builder()
                    .macdLine(Double.NaN)
                    .signalLine(Double.NaN)
                    .histogram(Double.NaN)
                    .build();
        }

        double ema12 = ema.calculate(prices, 12);
        double ema26 = ema.calculate(prices, 26);
        double macdLine = ema12 - ema26;

        // Build MACD series for signal line calculation
        List<Double> macdSeries = new ArrayList<>();
        for (int i = prices.size() - 26; i < prices.size(); i++) {
            List<Double> sub = prices.subList(0, i);
            macdSeries.add(ema.calculate(sub, 12) - ema.calculate(sub, 26));
        }

        double signalLine = ema.calculate(macdSeries, 9);
        double histogram = macdLine - signalLine;

        return MACDValues.builder()
                .macdLine(macdLine)
                .signalLine(signalLine)
                .histogram(histogram)
                .build();
    }
}