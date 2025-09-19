package com.project.bank_account_management_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO per la creazione di un nuovo utente con password")
public class CreateUtenteDTO {

    @NotBlank(message = "Il nome è obbligatorio")
    @Size(max = 100, message = "Il nome non può superare i 100 caratteri")
    @Schema(description = "Nome dell'utente", example = "Mario", required = true)
    private String nome;

    @NotBlank(message = "Il cognome è obbligatorio")
    @Size(max = 100, message = "Il cognome non può superare i 100 caratteri")
    @Schema(description = "Cognome dell'utente", example = "Rossi", required = true)
    private String cognome;

    @Past(message = "La data di nascita deve essere nel passato")
    @Schema(description = "Data di nascita dell'utente", example = "1985-03-15")
    private LocalDate dataNascita;

    @Size(max = 100, message = "La professione non può superare i 100 caratteri")
    @Schema(description = "Professione dell'utente", example = "Ingegnere")
    private String professione;

    @NotBlank(message = "Il codice fiscale è obbligatorio")
    @Size(min = 16, max = 16, message = "Il codice fiscale deve essere di 16 caratteri")
    @Schema(description = "Codice fiscale dell'utente", example = "RSSMRA85C15H501Z", required = true)
    private String codiceFiscale;

    @Schema(description = "Indirizzo dell'utente", example = "Via Roma 123")
    private String indirizzo;

    @Size(max = 100, message = "La città non può superare i 100 caratteri")
    @Schema(description = "Città dell'utente", example = "Milano")
    private String citta;

    @Size(max = 10, message = "Il CAP non può superare i 10 caratteri")
    @Schema(description = "CAP dell'utente", example = "20100")
    private String cap;

    @Size(max = 10, message = "La provincia non può superare i 10 caratteri")
    @Schema(description = "Provincia dell'utente", example = "MI")
    private String provincia;

    @Size(max = 100, message = "La nazione non può superare i 100 caratteri")
    @Schema(description = "Nazione dell'utente", example = "Italia")
    private String nazione;

    @Size(max = 20, message = "Il telefono non può superare i 20 caratteri")
    @Schema(description = "Numero di telefono dell'utente", example = "+39 123 456 7890")
    private String telefono;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "L'email deve essere valida")
    @Schema(description = "Email dell'utente", example = "mario.rossi@email.com", required = true)
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    @Size(min = 6, message = "La password deve essere di almeno 6 caratteri")
    @Schema(description = "Password dell'utente in chiaro (sarà crittografata automaticamente)",
            example = "password123", required = true)
    private String password;
}
