package com.project.bank_account_management_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUtenteDTO {

    @NotBlank(message = "Il nome è obbligatorio")
    @Size(max = 100, message = "Il nome non può superare 100 caratteri")
    private String nome;

    @NotBlank(message = "Il cognome è obbligatorio")
    @Size(max = 100, message = "Il cognome non può superare 100 caratteri")
    private String cognome;

    @Past(message = "La data di nascita deve essere nel passato")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascita;

    @Size(max = 100, message = "La professione non può superare 100 caratteri")
    private String professione;

    @NotBlank(message = "Il codice fiscale è obbligatorio")
    @Size(min = 16, max = 16, message = "Il codice fiscale deve essere di 16 caratteri")
    @Pattern(regexp = "^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$",
            message = "Formato codice fiscale non valido")
    private String codiceFiscale;

    private String indirizzo;

    @Size(max = 100, message = "La città non può superare 100 caratteri")
    private String citta;

    @Size(max = 10, message = "Il CAP non può superare 10 caratteri")
    @Pattern(regexp = "^[0-9]{5}$", message = "Il CAP deve essere di 5 cifre")
    private String cap;

    @Size(max = 10, message = "La provincia non può superare 10 caratteri")
    private String provincia;

    @Size(max = 100, message = "La nazione non può superare 100 caratteri")
    private String nazione;

    @Size(max = 20, message = "Il telefono non può superare 20 caratteri")
    @Pattern(regexp = "^[+]?[0-9\\s\\-()]*$", message = "Formato telefono non valido")
    private String telefono;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Formato email non valido")
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    @Size(min = 8, message = "La password deve essere di almeno 8 caratteri")
    private String password;

    // Campi per il documento di identità (opzionali)
    @Size(max = 50, message = "Il tipo documento non può superare 50 caratteri")
    private String tipoDocumento; // Es: "CARTA_IDENTITA", "PATENTE", "PASSAPORTO"

    @Size(max = 50, message = "Il numero documento non può superare 50 caratteri")
    private String numeroDocumento;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataRilascioDocumento;

    @Future(message = "La data di scadenza del documento deve essere futura")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataScadenzaDocumento;

    @Size(max = 100, message = "Il campo 'rilasciato da' non può superare 100 caratteri")
    private String rilasciatoDa; // Es: "Comune di Milano", "Motorizzazione Civile"

    // Validation personalizzata per i documenti
    @AssertTrue(message = "Se viene specificato un tipo documento, è necessario fornire anche il numero")
    public boolean isDocumentValid() {
        if (tipoDocumento != null && !tipoDocumento.trim().isEmpty()) {
            return numeroDocumento != null && !numeroDocumento.trim().isEmpty();
        }
        return true;
    }

    @AssertTrue(message = "La data di scadenza deve essere successiva alla data di rilascio")
    public boolean isDateValid() {
        if (dataRilascioDocumento != null && dataScadenzaDocumento != null) {
            return dataScadenzaDocumento.isAfter(dataRilascioDocumento);
        }
        return true;
    }
}