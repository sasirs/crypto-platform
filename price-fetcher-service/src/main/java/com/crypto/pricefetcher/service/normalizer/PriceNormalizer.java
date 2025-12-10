package com.crypto.pricefetcher.service.normalizer;

import com.crypto.pricefetcher.model.CryptoPriceEvent;
import com.crypto.pricefetcher.model.ProviderType;
import org.springframework.stereotype.Component;

/**
 * Component responsible for transforming raw price data into standardized
 * events.
 * Ensures consistent timestamping and currency tagging.
 */
@Component
public class PriceNormalizer {

    /**
     * Creates a CryptoPriceEvent from raw data.
     *
     * @param coin     The cryptocurrency symbol (e.g., "bitcoin").
     * @param price    The fetched price in USD.
     * @param provider The source provider of the data.
     * @return A fully constructed event object.
     */
    public CryptoPriceEvent normalize(String coin, Double price, ProviderType provider) {
        return CryptoPriceEvent.builder()
                .symbol(coin)
                .price(price)
                .currency("USD")
                .source(provider)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
