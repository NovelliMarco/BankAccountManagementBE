package com.project.bank_account_management_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO per la creazione di un nuovo conto da parte di un operatore")
public class CreateContoDTO {

    @NotBlank(message = "Il codice fiscale del titolare è obbligatorio")
    @Size(min = 16, max = 16, message = "Il codice fiscale deve essere di 16 caratteri")
    @Pattern(regexp = "^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$",
            message = "Formato codice fiscale non valido")
    @Schema(description = "Codice fiscale del titolare del conto", example = "RSSMRA85M01H501Z")
    private String codiceFiscaleTitolare;

    @NotBlank(message = "L'IBAN è obbligatorio")
    @Size(min = 27, max = 34, message = "L'IBAN deve essere tra 27 e 34 caratteri")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z0-9]+$",
            message = "Formato IBAN non valido")
    @Schema(description = "IBAN del nuovo conto", example = "IT60X0542811101000000123456")
    private String iban;

    @NotBlank(message = "Il tipo di conto è obbligatorio")
    @Size(max = 100, message = "Il tipo di conto non può superare i 100 caratteri")
    @Schema(description = "Tipologia del conto",
            example = "CONTO_CORRENTE",
            allowableValues = {"CONTO_CORRENTE", "CONTO_DEPOSITO", "CONTO_BUSINESS", "CONTO_GIOVANI"})
    private String tipoConto;

    @DecimalMin(value = "0.00", message = "Il saldo iniziale non può essere negativo")
    @Digits(integer = 16, fraction = 2, message = "Il saldo deve avere massimo 16 cifre intere e 2 decimali")
    @Schema(description = "Saldo iniziale del conto", example = "1000.00")
    @Builder.Default
    private BigDecimal saldoIniziale = BigDecimal.ZERO;

    @Size(max = 500, message = "Le note non possono superare i 500 caratteri")
    @Schema(description = "Note aggiuntive per la creazione del conto",
            example = "Conto aperto per nuovo cliente")
    private String note;

    @Schema(description = "Flag per indicare se il conto deve essere attivato immediatamente",
            example = "true")
    @Builder.Default
    private Boolean attivaImmediatamente = true;

    @Size(max = 100, message = "Il motivo di apertura non può superare i 100 caratteri")
    @Schema(description = "Motivo dell'apertura del conto",
            example = "NUOVA_RELAZIONE_BANCARIA",
            allowableValues = {"NUOVA_RELAZIONE_BANCARIA", "CONTO_AGGIUNTIVO", "MIGRAZIONE_BANCA", "ALTRO"})
    private String motivoApertura;

    // Campi aggiuntivi per configurazioni specifiche
    @Schema(description = "Limite giornaliero per operazioni (se applicabile)")
    private BigDecimal limiteGiornaliero;

    @Schema(description = "Limite mensile per operazioni (se applicabile)")
    private BigDecimal limiteMensile;

    @Schema(description = "Flag per abilitare l'internet banking", example = "true")
    @Builder.Default
    private Boolean internetBankingAbilitato = true;

    @Schema(description = "Flag per abilitare le notifiche SMS", example = "false")
    @Builder.Default
    private Boolean notificheSMS = false;

    @Schema(description = "Flag per abilitare le notifiche email", example = "true")
    @Builder.Default
    private Boolean notificheEmail = true;
}