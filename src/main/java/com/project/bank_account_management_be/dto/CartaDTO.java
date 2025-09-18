package com.project.bank_account_management_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO per la gestione delle carte di pagamento")
public class CartaDTO {

    @Schema(description = "ID univoco della carta", example = "1")
    private Integer id;

    @NotBlank(message = "Il numero carta è obbligatorio")
    @Size(min = 16, max = 16, message = "Il numero carta deve essere di 16 cifre")
    @Schema(description = "Numero della carta di pagamento", example = "1234567890123456", required = true)
    private String numeroCarta;

    @NotBlank(message = "Il tipo carta è obbligatorio")
    @Size(max = 20, message = "Il tipo carta non può superare i 20 caratteri")
    @Schema(description = "Tipo di carta", example = "VISA", required = true, allowableValues = {"VISA", "MASTERCARD", "AMERICAN_EXPRESS"})
    private String tipoCarta;

    @NotNull(message = "La data di scadenza è obbligatoria")
    @Schema(description = "Data di scadenza della carta", example = "2027-12-31", required = true)
    private LocalDate dataScadenza;

    @DecimalMin(value = "0.0", inclusive = true, message = "Il limite giornaliero non può essere negativo")
    @Schema(description = "Limite di spesa giornaliero", example = "500.00")
    private BigDecimal limiteGiornaliero;

    @DecimalMin(value = "0.0", inclusive = true, message = "Il limite mensile non può essere negativo")
    @Schema(description = "Limite di spesa mensile", example = "3000.00")
    private BigDecimal limiteMensile;

    @DecimalMin(value = "0.0", inclusive = true, message = "Il limite annuale non può essere negativo")
    @Schema(description = "Limite di spesa annuale", example = "30000.00")
    private BigDecimal limiteAnnuale;

    @Schema(description = "Indica se la carta è bloccata", example = "false")
    private Boolean bloccata;

    @Schema(description = "Stato della carta", example = "ATTIVA", allowableValues = {"ATTIVA", "BLOCCATA", "SCADUTA", "CHIUSA"})
    private String stato;

    @Schema(description = "Data di creazione della carta", example = "2024-01-15T10:30:00")
    private LocalDateTime dataCreazione;
}