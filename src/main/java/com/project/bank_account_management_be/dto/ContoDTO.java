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
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO per la gestione dei conti bancari")
public class ContoDTO {

    @Schema(description = "ID univoco del conto", example = "1")
    private Integer id;

    @NotBlank(message = "L'IBAN è obbligatorio")
    @Size(max = 34, message = "L'IBAN non può superare i 34 caratteri")
    @Schema(description = "IBAN del conto bancario", example = "IT60 X054 2811 1010 0000 0123 456", required = true)
    private String iban;

    @NotNull(message = "L'ID utente è obbligatorio")
    @Schema(description = "ID dell'utente proprietario del conto", example = "1", required = true)
    private Integer utenteId;

    @NotBlank(message = "Il tipo conto è obbligatorio")
    @Size(max = 100, message = "Il tipo conto non può superare i 100 caratteri")
    @Schema(description = "Tipologia del conto", example = "CONTO_CORRENTE", required = true)
    private String tipoConto;

    @NotNull(message = "Il saldo disponibile è obbligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "Il saldo disponibile non può essere negativo")
    @Schema(description = "Saldo disponibile del conto", example = "1500.50", required = true)
    private BigDecimal saldoDisponibile;

    @NotNull(message = "Il saldo contabile è obbligatorio")
    @Schema(description = "Saldo contabile del conto", example = "1500.50", required = true)
    private BigDecimal saldoContabile;

    @Schema(description = "Data di apertura del conto", example = "2024-01-15T10:30:00")
    private LocalDateTime dataApertura;

    @Schema(description = "Stato del conto", example = "ATTIVO")
    private String stato;

    @Schema(description = "Indica se il conto è aperto", example = "true")
    private Boolean contoAperto;

    @Schema(description = "Data di creazione del record", example = "2024-01-15T10:30:00")
    private LocalDateTime dataCreazione;
}