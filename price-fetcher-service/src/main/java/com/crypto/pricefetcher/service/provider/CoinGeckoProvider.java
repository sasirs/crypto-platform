package com.crypto.pricefetcher.service.provider;

import com.crypto.pricefetcher.exception.ApiProviderException;
import com.crypto.pricefetcher.model.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of PriceProvider for the CoinGecko API.
 * Uses WebClient for non-blocking HTTP requests.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CoinGeckoProvider implements PriceProvider {

    private final WebClient webClient;
    private static final String API_URL_TEMPLATE = "https://api.coingecko.com/api/v3/simple/price?vs_currencies=usd&ids=%s&x_cg_demo_api_key=CG-fcj5xiV5DNiojP9jJmT4GZeS";

    @Override
    public Map<String, Double> fetchPrices(List<String> coins) {
        String coinParam = String.join(",", coins);
        String url = String.format(API_URL_TEMPLATE, coinParam);

        log.debug("Calling CoinGecko API: {}", url);

        return webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> {
                            log.error("Error response from CoinGecko: {}", clientResponse.statusCode());
                            return Mono.error(new ApiProviderException(
                                    "CoinGecko API returned error: " + clientResponse.statusCode()));
                        })
                .bodyToMono(new ParameterizedTypeReference<Map<String, Map<String, Double>>>() {
                })
                .map(this::parseResponse)
                .onErrorResume(e -> {
                    log.error("Failed to fetch from CoinGecko: {}", e.getMessage());
                    return Mono.empty();
                })
                .block();
    }

    private Map<String, Double> parseResponse(Map<String, Map<String, Double>> response) {
        Map<String, Double> mapped = new HashMap<>();
        if (response != null) {
            response.forEach((coin, data) -> {
                if (data != null && data.containsKey("usd")) {
                    mapped.put(coin, data.get("usd"));
                }
            });
        }
        return mapped;
    }

    @Override
    public ProviderType getProviderName() {
        return ProviderType.COINGECKO;
    }
}
