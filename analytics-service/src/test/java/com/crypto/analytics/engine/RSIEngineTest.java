package com.crypto.analytics.engine;


import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RSIEngineTest {

    private final RSIEngine engine = new RSIEngine();

    @Test
    void testRSI_BasicCalculation() {
        List<Double> prices = Arrays.asList(
                44.0, 45.0, 46.0, 47.0, 48.0,
                47.0, 46.0, 45.0, 44.0, 43.0,
                44.0, 45.0, 46.0, 47.0, 48.0
        );

        double rsi = engine.calculate(prices);

        assertTrue(rsi >= 0 && rsi <= 100);
    }

    @Test
    void testRSI_NotEnoughData() {
        List<Double> prices = Arrays.asList(10.0, 12.0, 13.0);
        assertTrue(Double.isNaN(engine.calculate(prices)));
    }
}