package com.crypto.analytics.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MACDValues {
    private double macdLine;
    private double signalLine;
    private double histogram;
}
