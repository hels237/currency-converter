package com.k48.currency_converter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Représente la réponse de la conversion de devises")
public class ConversionResponseDTO {

    @Schema(description = "Montant de la devise source", example = "100.00")
    private double sourceAmount;

    @Schema(description = "Code de la devise source", example = "USD")
    private String sourceCurrency;

    @Schema(description = "Montant de la devise cible après conversion", example = "93.45")
    private double convertedAmount;

    @Schema(description = "Code de la devise cible", example = "EUR")
    private String targetCurrency;

    @Schema(description = "Taux de conversion utilisé (Source vers Cible)", example = "0.9345")
    private double exchangeRate;

    @Schema(description = "Date et heure de la conversion", example = "2023-10-27T10:30:00")
    private LocalDateTime conversionTime;
}