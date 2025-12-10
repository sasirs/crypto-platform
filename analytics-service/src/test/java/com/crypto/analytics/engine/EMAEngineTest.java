package com.crypto.analytics.engine;


import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EMAEngineTest {

    private final EMAEngine engine = new EMAEngine();

    @Test
    void testEMA_Calculation() {
        List<Double> prices = Arrays.asList(10.0, 20.0, 30.0, 40.0);

        double ema = engine.calculate(prices, 3);

        assertTrue(ema > 25.0 && ema < 40.0);  // lightweight test
    }

    @Test
    void testEMA_NotEnoughData() {
        List<Double> prices = Arrays.asList(100.0);
        assertTrue(Double.isNaN(engine.calculate(prices, 5)));
    }
}