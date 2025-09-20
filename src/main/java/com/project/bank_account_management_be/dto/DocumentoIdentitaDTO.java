package com.project.bank_account_management_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoIdentitaDTO {

    private Integer id;

    @NotNull(message = "L'ID utente è obbligatorio")
    private Integer utenteId;

    // Informazioni utente (per display)
    private String nomeUtente;
    private String cognomeUtente;
    private String codiceFiscaleUtente;

    @NotBlank(message = "Il tipo documento è obbligatorio")
    @Size(max = 50, message = "Il tipo documento non può superare 50 caratteri")
    @Pattern(regexp = "^(CARTA_IDENTITA|PATENTE|PASSAPORTO|PERMESSO_SOGGIORNO)$",
            message = "Tipo documento non valido")
    private String tipoDocumento;

    @NotBlank(message = "Il numero documento è obbligatorio")
    @Size(max = 50, message = "Il numero documento non può superare 50 caratteri")
    private String numeroDocumento;

    @Past(message = "La data di rilascio deve essere nel passato")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataRilascio;

    @Future(message = "La data di scadenza deve essere futura")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataScadenza;

    @Size(max = 100, message = "Il campo 'rilasciato da' non può superare 100 caratteri")
    private String rilasciatoDa;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCreazione;

    // Helper methods
    public String getTipoDocumentoDescrizione() {
        if (tipoDocumento != null) {
            switch (tipoDocumento) {
                case "CARTA_IDENTITA": return "Carta d'Identità";
                case "PATENTE": return "Patente di Guida";
                case "PASSAPORTO": return "Passaporto";
                case "PERMESSO_SOGGIORNO": return "Permesso di Soggiorno";
                default: return tipoDocumento;
            }
        }
        return "Tipo Sconosciuto";
    }

    public String getNumeroDocumentoMasked() {
        if (numeroDocumento != null && numeroDocumento.length() > 4) {
            return "****" + numeroDocumento.substring(numeroDocumento.length() - 4);
        }
        return "****";
    }

    public boolean isScaduto() {
        if (dataScadenza != null) {
            return dataScadenza.isBefore(LocalDate.now());
        }
        return false;
    }

    public boolean isInScadenza() {
        if (dataScadenza != null) {
            LocalDate oggi = LocalDate.now();
            LocalDate limiteScadenza = oggi.plusDays(30); // 30 giorni di preavviso
            return dataScadenza.isAfter(oggi) && dataScadenza.isBefore(limiteScadenza);
        }
        return false;
    }

    public String getNomeCompletoUtente() {
        if (nomeUtente != null && cognomeUtente != null) {
            return nomeUtente + " " + cognomeUtente;
        }
        return "";
    }

    // Validation personalizzata
    @AssertTrue(message = "La data di scadenza deve essere successiva alla data di rilascio")
    public boolean isDateValid() {
        if (dataRilascio != null && dataScadenza != null) {
            return dataScadenza.isAfter(dataRilascio);
        }
        return true;
    }
}