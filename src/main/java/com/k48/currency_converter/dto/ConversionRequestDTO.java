package com.k48.currency_converter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Génère les getters, setters, toString, equals, hashCode
@NoArgsConstructor // Génère un constructeur sans arguments
@AllArgsConstructor // Génère un constructeur avec tous les arguments
@Schema(description = "Représente la requête de conversion de devises")
public class ConversionRequestDTO {

    @NotBlank(message = "La devise source ne peut pas être vide.")
    @Pattern(regexp = "^[A-Z]{3}$", message = "La devise source doit être un code ISO 4217 de 3 lettres majuscules (ex: USD, EUR).")
    @Schema(description = "Code de la devise source (ISO 4217, ex: USD, EUR)", example = "USD")
    private String sourceCurrency;

    @NotBlank(message = "La devise cible ne peut pas être vide.")
    @Pattern(regexp = "^[A-Z]{3}$", message = "La devise cible doit être un code ISO 4217 de 3 lettres majuscules (ex: GBP, JPY).")
    @Schema(description = "Code de la devise cible (ISO 4217, ex: EUR, GBP)", example = "FCFA")
    private String targetCurrency;

    @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à zéro.")
    @Schema(description = "Montant à convertir", example = "100.00")
    private double amount;
}
