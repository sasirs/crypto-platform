package com.crypto.pricefetcher.service.provider;

import com.crypto.pricefetcher.model.ProviderType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class KuCoinProviderTest {

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    private KuCoinProvider provider;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        provider = new KuCoinProvider(webClient);
    }

    @Test
    void testFetchPrices_Success() {
        // Given
        List<String> coins = Collections.singletonList("bitcoin");
        String jsonResponse = "{\"data\": {\"BTC\": \"60000.0\"}}";
        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(jsonResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.just(jsonNode));

        // When
        Map<String, Double> result = provider.fetchPrices(coins);

        // Then
        assertNotNull(result);
        assertEquals(60000.0, result.get("bitcoin"));
    }

    @Test
    void testGetProviderName() {
        assertEquals(ProviderType.KUCOIN, provider.getProviderName());
    }
}
