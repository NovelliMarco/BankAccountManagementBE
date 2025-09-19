package com.project.bank_account_management_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO per il recupero password")
public class RecuperoPasswordDTO {

    @NotBlank(message = "Il nome è obbligatorio")
    @Size(max = 100, message = "Il nome non può superare i 100 caratteri")
    @Schema(description = "Nome dell'utente", example = "Mario", required = true)
    private String nome;

    @NotBlank(message = "Il cognome è obbligatorio")
    @Size(max = 100, message = "Il cognome non può superare i 100 caratteri")
    @Schema(description = "Cognome dell'utente", example = "Rossi", required = true)
    private String cognome;

    @NotBlank(message = "Il codice fiscale è obbligatorio")
    @Size(min = 16, max = 16, message = "Il codice fiscale deve essere di 16 caratteri")
    @Schema(description = "Codice fiscale dell'utente", example = "RSSMRA85C15H501Z", required = true)
    private String codiceFiscale;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "L'email deve essere valida")
    @Schema(description = "Email dell'utente", example = "mario.rossi@email.com", required = true)
    private String email;
}