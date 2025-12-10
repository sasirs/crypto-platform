package com.crypto.pricefetcher.service.normalizer;

import com.crypto.pricefetcher.model.CryptoPriceEvent;
import com.crypto.pricefetcher.model.ProviderType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriceNormalizerTest {

    private final PriceNormalizer normalizer = new PriceNormalizer();

    @Test
    void testNormalize() {
        // Given
        String coin = "bitcoin";
        Double price = 50000.0;
        ProviderType provider = ProviderType.COINGECKO;

        // When
        CryptoPriceEvent event = normalizer.normalize(coin, price, provider);

        // Then
        assertNotNull(event);
        assertEquals(coin, event.getSymbol());
        assertEquals(price, event.getPrice());
        assertEquals("USD", event.getCurrency());
        assertEquals(provider, event.getSource());
        assertTrue(event.getTimestamp() > 0);
    }
}
