package com.crypto.pricefetcher.exception;

public class ProviderFallbackException extends RuntimeException {
    public ProviderFallbackException(String message) {
        super(message);
    }
}
