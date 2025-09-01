package com.k48.currency_converter.service;

import com.k48.currency_converter.exception.ExchangeRateApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExchangeRateApiClient implements ExchangeRateProvider {

    private final WebClient webClient;

    @Value("${exchangerate.api.key}")
    private String apiKey;

    @Override
    public Mono<Double> getConversionRate(String baseCurrency, String targetCurrency) {
        String uri = String.format("%s/pair/%s/%s", apiKey, baseCurrency, targetCurrency);
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(response -> {
                    if ("success".equals(response.get("result"))) {
                        Object rate = response.get("conversion_rate");
                        if (rate instanceof Number) {
                            return Mono.just(((Number) rate).doubleValue());
                        } else {
                            return Mono.error(new ExchangeRateApiException("Taux de conversion invalide ou manquant de l'API externe."));
                        }
                    } else {
                        String errorType = (String) response.get("error-type");
                        return Mono.error(new ExchangeRateApiException("Erreur de l'API de taux de change: " + errorType));
                    }
                })
                .onErrorResume(WebClientResponseException.class, ex -> {
                    return Mono.error(new ExchangeRateApiException("Erreur de connexion à l'API de taux de change: " + ex.getStatusCode() + " - " + ex.getStatusText(), ex));
                })
                .onErrorResume(Exception.class, ex -> {
                    return Mono.error(new ExchangeRateApiException("Erreur inattendue lors de l'appel à l'API de taux de change.", ex));
                });
    }
}