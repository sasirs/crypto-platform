package com.crypto.analytics.controller;

import com.crypto.analytics.model.AnalyticsEvent;
import com.crypto.analytics.model.ApiResponse;
import com.crypto.analytics.service.AnalyticsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    private final AnalyticsQueryService queryService;

    @GetMapping("/symbols")
    public ApiResponse<Set<String>> getSymbols() {
        return ApiResponse.success(queryService.getTrackedSymbols());
    }

    @GetMapping("/{symbol}/latest")
    public ApiResponse<AnalyticsEvent> getLatest(@PathVariable String symbol) {
        AnalyticsEvent ev = queryService.getLatest(symbol);

        if (ev == null)
            return ApiResponse.error("No analytics available for symbol: " + symbol);

        return ApiResponse.success(ev);
    }

    @GetMapping("/{symbol}/history")
    public ApiResponse<List<AnalyticsEvent>> getHistory(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "50") int limit) {
        return ApiResponse.success(queryService.getHistory(symbol, limit));
    }

    @GetMapping("/health")
    public ApiResponse<String> health() {
        return ApiResponse.success("Analytics Service is running");
    }
}