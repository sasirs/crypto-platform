package com.crypto.pricefetcher.service.provider;

import com.crypto.pricefetcher.model.ProviderType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of PriceProvider for Binance API.
 * Defines mapping logic for symbols and handles individual coin requests.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BinanceProvider implements PriceProvider {

    private final WebClient webClient;
    private static final String API_URL_TEMPLATE = "https://api.binance.com/api/v3/ticker/price?symbol=%s";

    @Override
    public Map<String, Double> fetchPrices(List<String> coins) {
        Map<String, Double> prices = new HashMap<>();

        for (String coin : coins) {
            String symbol = mapToBinanceSymbol(coin);
            String url = String.format(API_URL_TEMPLATE, symbol);

            try {
                // Fetch price for individual symbol logic as Binance public ticker endpoint
                // typically works per symbol or all symbols (heavy payload).
                // Iterating per tailored list is safer for specific tracking.
                Double price = webClient.get()
                        .uri(url)
                        .retrieve()
                        .bodyToMono(JsonNode.class)
                        .map(node -> node.get("price").asDouble())
                        .onErrorResume(ex -> {
                            log.warn("Failed to fetch price for {} from Binance: {}", coin, ex.getMessage());
                            return Mono.empty();
                        })
                        .block();

                if (price != null) {
                    prices.put(coin, price);
                }
            } catch (Exception e) {
                log.error("Error calling Binance for {}: {}", coin, e.getMessage());
            }
        }
        return prices;
    }

    /**
     * Maps standard internal coin names to Binance trading pairs (e.g., bitcoin ->
     * BTCUSDT).
     */
    private String mapToBinanceSymbol(String coin) {
        // Simple mapping structure; could be moved to config or database in future
        if (coin.equalsIgnoreCase("bitcoin"))
            return "BTCUSDT";
        if (coin.equalsIgnoreCase("ethereum"))
            return "ETHUSDT";
        if (coin.equalsIgnoreCase("solana"))
            return "SOLUSDT";
        if (coin.equalsIgnoreCase("binancecoin"))
            return "BNBUSDT";
        if (coin.equalsIgnoreCase("cardano"))
            return "ADAUSDT";
        if (coin.equalsIgnoreCase("dogecoin"))
            return "DOGEUSDT";

        // Fallback default assumption
        return coin.toUpperCase() + "USDT";
    }

    @Override
    public ProviderType getProviderName() {
        return ProviderType.BINANCE;
    }
}
