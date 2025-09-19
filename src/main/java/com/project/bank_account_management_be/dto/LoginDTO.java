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

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "L'email deve essere valida")
    @Schema(description = "Email dell'utente", example = "mario.rossi@email.com", required = true)
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    @Size(min = 6, message = "La password deve essere di almeno 6 caratteri")
    @Schema(description = "Password dell'utente", example = "password123", required = true)
    private String password;
}