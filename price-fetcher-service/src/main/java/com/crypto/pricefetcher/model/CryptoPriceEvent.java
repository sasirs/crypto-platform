package com.crypto.pricefetcher.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CryptoPriceEvent {
    private String symbol;
    private double price;
    private String currency;
    private ProviderType source;
    private long timestamp;
}
