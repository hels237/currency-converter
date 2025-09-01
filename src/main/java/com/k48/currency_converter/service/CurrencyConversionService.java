package com.k48.currency_converter.service;

import com.k48.currency_converter.dto.ConversionRequestDTO;
import com.k48.currency_converter.dto.ConversionResponseDTO;
import com.k48.currency_converter.exception.ExchangeRateApiException; // Bien que nous relancions en RuntimeException, l'import peut être utile pour les erreurs spécifiques
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CurrencyConversionService {

    // Injecte l'INTERFACE, et non l'implémentation concrète !
    private final ExchangeRateProvider exchangeRateProvider;

    public Mono<ConversionResponseDTO> convertCurrency(ConversionRequestDTO request) {
        String sourceCurrency = request.getSourceCurrency().toUpperCase();
        String targetCurrency = request.getTargetCurrency().toUpperCase();
        double amount = request.getAmount();

        if (sourceCurrency.equals(targetCurrency)) {
            return Mono.just(new ConversionResponseDTO(
                    amount,
                    sourceCurrency,
                    amount,
                    targetCurrency,
                    1.0,
                    LocalDateTime.now()
            ));
        }

        // Utilise la méthode de l'interface
        return exchangeRateProvider.getConversionRate(sourceCurrency, targetCurrency)
                .map(exchangeRate -> {
                    double convertedAmount = amount * exchangeRate;
                    return new ConversionResponseDTO(
                            amount,
                            sourceCurrency,
                            convertedAmount,
                            targetCurrency,
                            exchangeRate,
                            LocalDateTime.now()
                    );
                })
                .onErrorResume(ExchangeRateApiException.class, ex -> {
                    return Mono.error(new RuntimeException("Impossible de convertir la devise: " + ex.getMessage(), ex));
                })
                .onErrorResume(Exception.class, ex -> {
                    return Mono.error(new RuntimeException("Une erreur inattendue est survenue lors de la conversion: " + ex.getMessage(), ex));
                });
    }
}