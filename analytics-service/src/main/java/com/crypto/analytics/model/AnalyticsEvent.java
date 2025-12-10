package com.crypto.analytics.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsEvent {
    private String symbol;
    private double price;
    private Instant timestamp;
    private IndicatorSet indicators;
    private SignalType signal;
}
