package com.crypto.analytics.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndicatorSet {
    private double sma20;
    private double ema20;
    private double rsi14;
    private MACDValues macd;
    private double volatility;
}
