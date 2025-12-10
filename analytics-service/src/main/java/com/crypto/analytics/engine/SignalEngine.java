package com.crypto.analytics.engine;

import com.crypto.analytics.model.MACDValues;
import com.crypto.analytics.model.SignalType;
import org.springframework.stereotype.Component;

@Component
public class SignalEngine {

    public SignalType evaluate(double rsi, MACDValues macd) {

        if (Double.isNaN(rsi) || Double.isNaN(macd.getHistogram())) {
            return SignalType.HOLD;
        }

        if (rsi < 30 && macd.getHistogram() > 0) {
            return SignalType.BUY;
        }

        if (rsi > 70 && macd.getHistogram() < 0) {
            return SignalType.SELL;
        }

        return SignalType.HOLD;
    }
}