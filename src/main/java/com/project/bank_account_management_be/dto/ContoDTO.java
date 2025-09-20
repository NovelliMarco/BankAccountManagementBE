package com.project.bank_account_management_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContoDTO {

    private Integer id;

    @NotBlank(message = "L'IBAN è obbligatorio")
    @Size(min = 15, max = 34, message = "L'IBAN deve essere tra 15 e 34 caratteri")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z0-9]+$", message = "Formato IBAN non valido")
    private String iban;

    @NotNull(message = "L'ID utente è obbligatorio")
    private Integer utenteId;

    // Informazioni utente (per display, non modificabili)
    private String nomeUtente;
    private String cognomeUtente;
    private String codiceFiscaleUtente;

    @NotBlank(message = "Il tipo conto è obbligatorio")
    @Size(max = 100, message = "Il tipo conto non può superare 100 caratteri")
    @Pattern(regexp = "^(CORRENTE|RISPARMIO|DEPOSITO|BUSINESS)$", message = "Tipo conto non valido")
    private String tipoConto;

    @NotNull(message = "Il saldo disponibile è obbligatorio")
    @DecimalMin(value = "0.0", message = "Il saldo disponibile non può essere negativo")
    @Digits(integer = 15, fraction = 2, message = "Il saldo deve avere massimo 15 cifre intere e 2 decimali")
    @Builder.Default
    private BigDecimal saldoDisponibile = BigDecimal.ZERO;

    @NotNull(message = "Il saldo contabile è obbligatorio")
    @Digits(integer = 15, fraction = 2, message = "Il saldo deve avere massimo 15 cifre intere e 2 decimali")
    @Builder.Default
    private BigDecimal saldoContabile = BigDecimal.ZERO;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataApertura;

    @Pattern(regexp = "^(ATTIVO|CHIUSO|SOSPESO|BLOCCATO)$", message = "Stato conto non valido")
    @Builder.Default
    private String stato = "ATTIVO";

    @Builder.Default
    private Boolean contoAperto = true;

    // Helper methods
    public String getIbanFormatted() {
        if (iban != null && iban.length() > 4) {
            // Formatta l'IBAN in gruppi di 4 caratteri
            return iban.replaceAll("(.{4})", "$1 ").trim();
        }
        return iban;
    }

    public String getIbanMasked() {
        if (iban != null && iban.length() > 8) {
            return iban.substring(0, 4) + " **** **** " + iban.substring(iban.length() - 4);
        }
        return iban;
    }

    public String getNomeCompletoUtente() {
        if (nomeUtente != null && cognomeUtente != null) {
            return nomeUtente + " " + cognomeUtente;
        }
        return "";
    }

    public String getStatoDescrizione() {
        if (stato != null) {
            switch (stato) {
                case "ATTIVO": return "Conto Attivo";
                case "CHIUSO": return "Conto Chiuso";
                case "SOSPESO": return "Conto Sospeso";
                case "BLOCCATO": return "Conto Bloccato";
                default: return stato;
            }
        }
        return "Stato Sconosciuto";
    }

    public String getTipoContoDescrizione() {
        if (tipoConto != null) {
            switch (tipoConto) {
                case "CORRENTE": return "Conto Corrente";
                case "RISPARMIO": return "Conto Risparmio";
                case "DEPOSITO": return "Conto Deposito";
                case "BUSINESS": return "Conto Business";
                default: return tipoConto;
            }
        }
        return "Tipo Sconosciuto";
    }

    // Validation personalizzata
    @AssertTrue(message = "Il saldo contabile deve essere maggiore o uguale al saldo disponibile")
    public boolean isSaldoValid() {
        if (saldoDisponibile != null && saldoContabile != null) {
            return saldoContabile.compareTo(saldoDisponibile) >= 0;
        }
        return true;
    }
}