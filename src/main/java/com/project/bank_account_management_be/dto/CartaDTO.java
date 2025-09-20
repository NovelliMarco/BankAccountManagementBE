package com.project.bank_account_management_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartaDTO {

    private Integer id;

    @Size(max = 16, message = "Il numero carta non può superare 16 caratteri")
    @Pattern(regexp = "^[0-9]{13,19}$", message = "Il numero carta deve contenere solo numeri tra 13 e 19 cifre")
    private String numeroCarta;

    @NotNull(message = "L'ID utente è obbligatorio")
    private Integer utenteId;

    // Informazioni utente (per display, non modificabili)
    private String nomeUtente;
    private String cognomeUtente;
    private String codiceFiscaleUtente;

    @NotBlank(message = "Il tipo carta è obbligatorio")
    @Size(max = 20, message = "Il tipo carta non può superare 20 caratteri")
    @Pattern(regexp = "^(DEBITO|CREDITO|PREPAGATA)$", message = "Tipo carta non valido")
    private String tipoCarta;

    @NotNull(message = "La data di scadenza è obbligatoria")
    @Future(message = "La data di scadenza deve essere futura")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataScadenza;

    @Size(min = 3, max = 3, message = "Il CVV deve essere di 3 cifre")
    @Pattern(regexp = "^[0-9]{3}$", message = "Il CVV deve contenere solo numeri")
    private String cvv;

    @DecimalMin(value = "0.0", inclusive = false, message = "Il limite giornaliero deve essere maggiore di zero")
    @DecimalMax(value = "10000.00", message = "Il limite giornaliero non può superare 10.000€")
    @Builder.Default
    private BigDecimal limiteGiornaliero = new BigDecimal("500.00");

    @DecimalMin(value = "0.0", inclusive = false, message = "Il limite mensile deve essere maggiore di zero")
    @DecimalMax(value = "50000.00", message = "Il limite mensile non può superare 50.000€")
    @Builder.Default
    private BigDecimal limiteMensile = new BigDecimal("3000.00");

    @DecimalMin(value = "0.0", inclusive = false, message = "Il limite annuale deve essere maggiore di zero")
    @DecimalMax(value = "100000.00", message = "Il limite annuale non può superare 100.000€")
    @Builder.Default
    private BigDecimal limiteAnnuale = new BigDecimal("30000.00");

    private Boolean bloccata;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataBlocco;

    @Pattern(regexp = "^(ATTIVA|BLOCCATA|SCADUTA|ANNULLATA)$", message = "Stato carta non valido")
    private String stato;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCreazione;

    // Validation personalizzata per i limiti
    @AssertTrue(message = "Il limite mensile deve essere maggiore o uguale al limite giornaliero")
    public boolean isLimiteMensileValid() {
        if (limiteGiornaliero != null && limiteMensile != null) {
            return limiteMensile.compareTo(limiteGiornaliero) >= 0;
        }
        return true;
    }

    @AssertTrue(message = "Il limite annuale deve essere maggiore o uguale al limite mensile")
    public boolean isLimiteAnnualeValid() {
        if (limiteMensile != null && limiteAnnuale != null) {
            return limiteAnnuale.compareTo(limiteMensile) >= 0;
        }
        return true;
    }

    // Helper methods per mascherare il numero carta
    public String getNumeroCartaMasked() {
        if (numeroCarta != null && numeroCarta.length() >= 4) {
            return "****-****-****-" + numeroCarta.substring(numeroCarta.length() - 4);
        }
        return "****-****-****-****";
    }

    public String getNomeCompletoUtente() {
        if (nomeUtente != null && cognomeUtente != null) {
            return nomeUtente + " " + cognomeUtente;
        }
        return "";
    }
}