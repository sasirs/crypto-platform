package com.crypto.analytics.engine;


import com.crypto.analytics.model.MACDValues;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MACDEngineTest {

    private final EMAEngine ema = new EMAEngine();
    private final MACDEngine macdEngine = new MACDEngine(ema);

    @Test
    void testMACD_ValidOutput() {

        List<Double> prices = Arrays.asList(
                10.0, 12.0, 11.0, 13.0, 12.0,
                14.0, 13.0, 15.0, 16.0, 17.0,
                16.0, 18.0, 17.0, 19.0, 18.0,
                20.0, 21.0, 22.0, 23.0, 24.0,
                23.0, 25.0, 26.0, 27.0, 28.0,
                29.0
        );

        MACDValues macd = macdEngine.calculate(prices);

        assertFalse(Double.isNaN(macd.getMacdLine()));
      //  assertFalse(Double.isNaN(macd.getSignalLine()));
      //  assertFalse(Double.isNaN(macd.getHistogram()));
    }

    @Test
    void testMACD_NotEnoughData() {
        List<Double> prices = Arrays.asList(10.0, 20.0, 30.0);

        MACDValues macd = macdEngine.calculate(prices);

        assertTrue(Double.isNaN(macd.getMacdLine()));
    }
}