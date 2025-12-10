package com.crypto.analytics.engine;


import com.crypto.analytics.model.MACDValues;
import com.crypto.analytics.model.SignalType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignalEngineTest {

    private final SignalEngine signalEngine = new SignalEngine();

    @Test
    void testBuySignal() {
        MACDValues macd = MACDValues.builder()
                .histogram(2.0)
                .build();

        SignalType signal = signalEngine.evaluate(25.0, macd);

        assertEquals(SignalType.BUY, signal);
    }

    @Test
    void testSellSignal() {
        MACDValues macd = MACDValues.builder()
                .histogram(-3.0)
                .build();

        SignalType signal = signalEngine.evaluate(80.0, macd);

        assertEquals(SignalType.SELL, signal);
    }

    @Test
    void testHoldSignal_Default() {
        MACDValues macd = MACDValues.builder()
                .histogram(0.5)
                .build();

        SignalType signal = signalEngine.evaluate(50.0, macd);

        assertEquals(SignalType.HOLD, signal);
    }
}