package com.crypto.notification.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsEvent {
    private String symbol;
    private Double price;
    private Long timestamp;
    private String signal; // BUY / SELL / HOLD
    private Map<String, Object> indicators;
}