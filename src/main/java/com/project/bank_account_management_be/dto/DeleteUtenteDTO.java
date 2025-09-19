package com.project.bank_account_management_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO per la cancellazione utente con validazione password")
public class DeleteUtenteDTO {

    @NotBlank(message = "La password è obbligatoria per la cancellazione")
    @Schema(description = "Password dell'utente per confermare la cancellazione",
            example = "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3",
            required = true)
    private String password;
}