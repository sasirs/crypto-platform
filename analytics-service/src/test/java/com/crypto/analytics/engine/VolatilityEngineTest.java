package com.crypto.analytics.engine;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VolatilityEngineTest {

    private final VolatilityEngine engine = new VolatilityEngine();

    @Test
    void testVolatility_Calculation() {
        List<Double> prices = Arrays.asList(10.0, 20.0, 30.0);

        double volatility = engine.calculate(prices);

        assertTrue(volatility > 0);
    }

    @Test
    void testVolatility_NotEnoughData() {
        List<Double> prices = Arrays.asList(10.0);
        assertTrue(Double.isNaN(engine.calculate(prices)));
    }
}