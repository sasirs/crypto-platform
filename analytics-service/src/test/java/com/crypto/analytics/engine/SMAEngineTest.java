package com.crypto.analytics.engine;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SMAEngineTest {

    private final SMAEngine engine = new SMAEngine();

    @Test
    void testSMA_CorrectCalculation() {
        List<Double> prices = Arrays.asList(10.0, 20.0, 30.0, 40.0);
        double sma = engine.calculate(prices, 4);
        assertEquals(25.0, sma, 0.0001);
    }

    @Test
    void testSMA_NotEnoughData() {
        List<Double> prices = Arrays.asList(10.0, 20.0);
        assertTrue(Double.isNaN(engine.calculate(prices, 4)));
    }
}