package com.project.bank_account_management_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO di risposta per il login")
public class LoginResponseDTO {

    @Schema(description = "Indica se il login è avvenuto con successo", example = "true")
    private boolean success;

    @Schema(description = "Messaggio di risposta", example = "Login effettuato con successo")
    private String message;

    @Schema(description = "Token JWT per l'autenticazione", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "ID dell'utente", example = "123")
    private Integer utenteId;

    @Schema(description = "Email dell'utente", example = "mario.rossi@email.com")
    private String email;

    @Schema(description = "Nome dell'utente", example = "Mario")
    private String nome;

    @Schema(description = "Cognome dell'utente", example = "Rossi")
    private String cognome;

    @Schema(description = "Ruolo dell'utente", example = "CLIENTE",
            allowableValues = {"AMMINISTRATORE", "OPERATORE", "CLIENTE"})
    private String ruolo;

    @Schema(description = "Timestamp di scadenza del token")
    private Long tokenExpiration;

    @Schema(description = "Permessi dell'utente")
    private java.util.List<String> permessi;
}