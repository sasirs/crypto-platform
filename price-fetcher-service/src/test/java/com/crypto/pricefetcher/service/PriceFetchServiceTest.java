package com.crypto.pricefetcher.service;

import com.crypto.pricefetcher.config.AppProperties;
import com.crypto.pricefetcher.model.CryptoPriceEvent;
import com.crypto.pricefetcher.model.ProviderType;
import com.crypto.pricefetcher.service.normalizer.PriceNormalizer;
import com.crypto.pricefetcher.service.provider.PriceProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PriceFetchServiceTest {

    @Mock
    private AppProperties appProperties;
    @Mock
    private PriceProvider coinGeckoProvider;
    @Mock
    private PriceProvider binanceProvider;

    private PriceFetchService priceFetchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup Providers
        when(coinGeckoProvider.getProviderName()).thenReturn(ProviderType.COINGECKO);
        when(binanceProvider.getProviderName()).thenReturn(ProviderType.BINANCE);

        List<PriceProvider> providers = Arrays.asList(coinGeckoProvider, binanceProvider);
        PriceNormalizer normalizer = new PriceNormalizer();

        priceFetchService = new PriceFetchService(appProperties, providers, normalizer);
    }

    @Test
    void testFetchLatestPrices_PrimarySuccess() {
        // Given
        when(appProperties.getPriorityProviders()).thenReturn(Arrays.asList("coingecko", "binance"));
        when(appProperties.getCoins()).thenReturn(Collections.singletonList("bitcoin"));

        Map<String, Double> mockPrices = new HashMap<>();
        mockPrices.put("bitcoin", 50000.0);
        when(coinGeckoProvider.fetchPrices(anyList())).thenReturn(mockPrices);

        // When
        Map<String, CryptoPriceEvent> events = priceFetchService.fetchLatestPrices();

        // Then
        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals(50000.0, events.get("bitcoin").getPrice());
        assertEquals(ProviderType.COINGECKO, events.get("bitcoin").getSource());

        verify(coinGeckoProvider, times(1)).fetchPrices(anyList());
        verify(binanceProvider, times(0)).fetchPrices(anyList()); // Should not call secondary
    }

    @Test
    void testFetchLatestPrices_PrimaryFail_FallbackToSecondary() {
        // Given
        when(appProperties.getPriorityProviders()).thenReturn(Arrays.asList("coingecko", "binance"));
        when(appProperties.getCoins()).thenReturn(Collections.singletonList("bitcoin"));

        // Primary fails
        when(coinGeckoProvider.fetchPrices(anyList())).thenThrow(new RuntimeException("API Error"));

        // Secondary succeeds
        Map<String, Double> mockPrices = new HashMap<>();
        mockPrices.put("bitcoin", 51000.0);
        when(binanceProvider.fetchPrices(anyList())).thenReturn(mockPrices);

        // When
        Map<String, CryptoPriceEvent> events = priceFetchService.fetchLatestPrices();

        // Then
        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals(51000.0, events.get("bitcoin").getPrice());
        assertEquals(ProviderType.BINANCE, events.get("bitcoin").getSource());

        verify(coinGeckoProvider, times(1)).fetchPrices(anyList());
        verify(binanceProvider, times(1)).fetchPrices(anyList());
    }
}
