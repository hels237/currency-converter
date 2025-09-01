package com.k48.currency_converter.controller;

import com.k48.currency_converter.dto.ConversionRequestDTO;
import com.k48.currency_converter.dto.ConversionResponseDTO;
import com.k48.currency_converter.exception.ExchangeRateApiException;
import com.k48.currency_converter.service.CurrencyConversionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/convert") // Chemin de base pour toutes les requêtes de ce contrôleur
@RequiredArgsConstructor
@Tag(name = "Conversion de Devises", description = "API pour convertir des montants entre différentes devises")
public class ConversionController {

    private final CurrencyConversionService currencyConversionService;

    @Operation(summary = "Convertit un montant d'une devise source vers une devise cible")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversion réussie",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConversionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide (devise manquante, montant invalide, etc.)",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur ou problème avec l'API de taux de change externe",
                    content = @Content)
    })
    @GetMapping // Mappe les requêtes GET sur ce chemin
    public Mono<ResponseEntity<ConversionResponseDTO>> convertCurrency(
            @Parameter(description = "Code ISO de la devise source (ex: USD)", example = "USD", required = true)
            @RequestParam @Valid @jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Pattern(regexp = "^[A-Z]{3}$") String source,
            @Parameter(description = "Code ISO de la devise cible (ex: EUR)", example = "EUR", required = true)
            @RequestParam @Valid @jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Pattern(regexp = "^[A-Z]{3}$") String target,
            @Parameter(description = "Montant à convertir", example = "100.00", required = true)
            @RequestParam @Valid @jakarta.validation.constraints.DecimalMin(value = "0.01") double amount) {

        // Crée l'objet de requête DTO
        ConversionRequestDTO requestDTO = new ConversionRequestDTO(source, target, amount);

        // Appelle le service pour effectuer la conversion
        return currencyConversionService.convertCurrency(requestDTO)
                .map(response -> ResponseEntity.ok(response)) // Retourne 200 OK avec la réponse
                .onErrorResume(RuntimeException.class, ex -> { // Capture les RuntimeException générées par le service
                    //  affiner la gestion d'erreurs ici, par exemple :
                    // Si l'exception est due à une devise inconnue, renvoyer 400 Bad Request
                    if (ex.getCause() instanceof ExchangeRateApiException && ex.getCause().getMessage().contains("devise invalide")) {
                        return Mono.just(ResponseEntity.badRequest().build()); // Ou un DTO d'erreur plus spécifique
                    }
                    // Pour toute autre erreur, renvoyer 500 Internal Server Error
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }
}