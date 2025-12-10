package com.crypto.pricefetcher.service.provider;

import com.crypto.pricefetcher.model.ProviderType;

import java.util.List;
import java.util.Map;

public interface PriceProvider {
    Map<String, Double> fetchPrices(List<String> coins);

    ProviderType getProviderName();
}
