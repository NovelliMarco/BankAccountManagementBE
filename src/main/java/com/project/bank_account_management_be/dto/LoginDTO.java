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
@Schema(description = "DTO per il login utente")
public class LoginDTO {

    @Schema(description = "ID del login", example = "1")
    private Integer id;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "L'email deve essere valida")
    @Schema(description = "Email dell'utente", example = "mario.rossi@email.com", required = true)
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    @Size(min = 64, max = 64, message = "La password deve essere l'hash SHA-256 di 64 caratteri")
    @Schema(description = "Password dell'utente già crittografata con SHA-256",
            example = "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3",
            required = true)
    private String password;

}