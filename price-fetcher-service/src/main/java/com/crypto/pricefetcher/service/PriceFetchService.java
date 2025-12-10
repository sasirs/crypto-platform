package com.crypto.pricefetcher.service;

import com.crypto.pricefetcher.config.AppProperties;
import com.crypto.pricefetcher.model.CryptoPriceEvent;
import com.crypto.pricefetcher.service.normalizer.PriceNormalizer;
import com.crypto.pricefetcher.service.provider.PriceProvider;
import com.crypto.pricefetcher.utils.CacheUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Core service responsible for orchestrating the fetching of cryptocurrency prices.
 * It handles the provider failover logic and ensures that prices are normalized and cached
 * before being returned for publishing.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PriceFetchService {

    private final AppProperties config;
    private final List<PriceProvider> providers;
    private final PriceNormalizer normalizer;
    private final CacheUtil cache = new CacheUtil();

    /**
     * Fetches the latest prices from the configured providers.
     * Iterates through the priority list of providers (e.g., CoinGecko -> Binance -> KuCoin).
     * If the primary provider fails, it seamlessly fails over to the next one.
     *
     * @return A map of Coin Symbol to CryptoPriceEvent objects ready for publishing.
     */
    public Map<String, CryptoPriceEvent> fetchLatestPrices() {
        Map<String, CryptoPriceEvent> events = new HashMap<>();

        // Iterate through providers based on the configured priority
        for (String providerName : config.getPriorityProviders()) {
            try {
                // Find the provider bean by name
                Optional<PriceProvider> providerOpt = getProvider(providerName);
                if (providerOpt.isEmpty()) {
                    log.warn("Configured provider '{}' not found in application context. Skipping.", providerName);
                    continue;
                }
                
                PriceProvider provider = providerOpt.get();
                log.debug("Attempting to fetch prices using provider: {}", providerName);

                Map<String, Double> prices = provider.fetchPrices(config.getCoins());
                
                // If the provider returned valid data, process it and stop the failover loop
                if (prices != null && !prices.isEmpty()) {
                    processPrices(prices, provider, events);
                    log.info("Successfully fetched {} prices from {}", prices.size(), providerName);
                    return events; 
                }
            } catch (Exception e) {
                // Log the failure and continue to the next provider in the list
                log.error("Failed to fetch prices from provider '{}': {}", providerName, e.getMessage());
            }
        }
        
        if (events.isEmpty()) {
            log.error("Critical: All configured providers failed to return price data.");
        }

        return events;
    }

    /**
     * Processes raw price data, checks for duplicates against the cache, and normalizes it.
     *
     * @param prices   Map of raw prices (Symbol -> Price)
     * @param provider The provider source of this data
     * @param events   Target map to populate with normalized events
     */
    private void processPrices(Map<String, Double> prices, PriceProvider provider, Map<String, CryptoPriceEvent> events) {
        prices.forEach((coin, price) -> {
            // Check if the price has changed since the last fetch to avoid spamming downstream
            if (!cache.isDuplicate(coin, price)) {
                CryptoPriceEvent event = normalizer.normalize(coin, price, provider.getProviderName());
                events.put(coin, event);
                
                // Update cache with the new price
                cache.update(coin, price);
            } else {
                log.trace("Skipping duplicate price for {}: {}", coin, price);
            }
        });
    }

    /**
     * Helper to find a PriceProvider bean by its enum name provided in config string.
     */
    private Optional<PriceProvider> getProvider(String name) {
        return providers.stream()
                .filter(p -> p.getProviderName().name().equalsIgnoreCase(name))
                .findFirst();
    }
}
