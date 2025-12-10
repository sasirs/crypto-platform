package com.crypto.pricefetcher.service.provider;

import com.crypto.pricefetcher.model.ProviderType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Implementation of PriceProvider for KuCoin API.
 * Handles the logic of querying multiple symbols via mapped parameter.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KuCoinProvider implements PriceProvider {

    private final WebClient webClient;
    private static final String API_URL_TEMPLATE = "https://api.kucoin.com/api/v1/prices?base=USD&currencies=%s";

    @Override
    public Map<String, Double> fetchPrices(List<String> coins) {
        StringBuilder symbolParam = new StringBuilder();
        Map<String, String> symbolToCoinName = new HashMap<>();

        // Prepare mapping and query parameter
        for (String coin : coins) {
            String symbol = getSymbol(coin);
            if (symbolParam.length() > 0)
                symbolParam.append(",");
            symbolParam.append(symbol);
            symbolToCoinName.put(symbol, coin);
        }

        String url = String.format(API_URL_TEMPLATE, symbolParam);
        log.debug("Calling KuCoin API: {}", url);

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(root -> parseResponse(root, symbolToCoinName))
                .onErrorResume(ex -> {
                    log.error("Failed to fetch prices from KuCoin: {}", ex.getMessage());
                    return Mono.empty();
                })
                .block();
    }

    private Map<String, Double> parseResponse(JsonNode root, Map<String, String> symbolToCoinName) {
        Map<String, Double> result = new HashMap<>();
        JsonNode data = root.get("data");

        if (data != null) {
            Iterator<String> fieldNames = data.fieldNames();
            while (fieldNames.hasNext()) {
                String sym = fieldNames.next();
                double price = data.get(sym).asDouble();
                String originalName = symbolToCoinName.get(sym);

                // If we find a mapping back to our internal name, store it
                if (originalName != null) {
                    result.put(originalName, price);
                }
            }
        }
        return result;
    }

    private String getSymbol(String coin) {
        if (coin.equalsIgnoreCase("bitcoin"))
            return "BTC";
        if (coin.equalsIgnoreCase("ethereum"))
            return "ETH";
        if (coin.equalsIgnoreCase("solana"))
            return "SOL";
        if (coin.equalsIgnoreCase("binancecoin"))
            return "BNB";
        if (coin.equalsIgnoreCase("cardano"))
            return "ADA";
        if (coin.equalsIgnoreCase("dogecoin"))
            return "DOGE";
        return coin.toUpperCase();
    }

    @Override
    public ProviderType getProviderName() {
        return ProviderType.KUCOIN;
    }
}
