package com.project.bank_account_management_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO per l'effettuazione di bonifici")
public class BonificoDTO {

    @NotBlank(message = "L'IBAN del beneficiario è obbligatorio")
    @Size(max = 34, message = "L'IBAN non può superare i 34 caratteri")
    @Schema(description = "IBAN del conto beneficiario", example = "IT60 X054 2811 1010 0000 0123 457", required = true)
    private String ibanBeneficiario;

    @NotNull(message = "L'importo è obbligatorio")
    @DecimalMin(value = "0.01", message = "L'importo deve essere maggiore di zero")
    @Schema(description = "Importo del bonifico", example = "150.75", required = true)
    private BigDecimal importo;

    @NotBlank(message = "La causale è obbligatoria")
    @Size(max = 500, message = "La causale non può superare i 500 caratteri")
    @Schema(description = "Causale del bonifico", example = "Pagamento fattura n. 123/2024", required = true)
    private String causale;

    @Size(max = 100, message = "Il nome del beneficiario non può superare i 100 caratteri")
    @Schema(description = "Nome del beneficiario", example = "Mario Rossi")
    private String nomeBeneficiario;
}