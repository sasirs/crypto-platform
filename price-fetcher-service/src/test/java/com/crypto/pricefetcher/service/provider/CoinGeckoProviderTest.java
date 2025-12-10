package com.crypto.pricefetcher.service.provider;

import com.crypto.pricefetcher.model.ProviderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CoinGeckoProviderTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private CoinGeckoProvider provider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        provider = new CoinGeckoProvider(webClient);
    }

    @Test
    void testFetchPrices_Success() {
        // Given
        List<String> coins = Collections.singletonList("bitcoin");
        Map<String, Map<String, Double>> apiResponse = new HashMap<>();
        Map<String, Double> priceMap = new HashMap<>();
        priceMap.put("usd", 50000.0);
        apiResponse.put("bitcoin", priceMap);

        // Mock WebClient Chain
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(org.springframework.core.ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(apiResponse));

        // When
        Map<String, Double> result = provider.fetchPrices(coins);

        // Then
        assertNotNull(result);
        assertEquals(50000.0, result.get("bitcoin"));
    }

    @Test
    void testGetProviderName() {
        assertEquals(ProviderType.COINGECKO, provider.getProviderName());
    }
}
