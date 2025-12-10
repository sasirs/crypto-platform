package com.crypto.pricefetcher.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class that maps application properties.
 * Prefixed with "app" in application.properties.
 */
@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {

    /** List of cryptocurrency symbols to fetch (e.g., bitcoin, ethereum). */
    private List<String> coins;

    /** Polling interval in milliseconds for the scheduler. */
    private int pollInterval;

    /** Ordered list of providers to use for fetching prices (priority failover). */
    private List<String> priorityProviders;
}
