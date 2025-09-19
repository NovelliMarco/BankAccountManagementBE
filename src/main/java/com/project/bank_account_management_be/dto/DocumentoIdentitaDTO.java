package com.project.bank_account_management_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO per i documenti di identità")
public class DocumentoIdentitaDTO {

    @Schema(description = "ID univoco del documento", example = "1")
    private Integer id;

    @NotNull(message = "L'ID utente è obbligatorio")
    @Schema(description = "ID dell'utente proprietario del documento", example = "1", required = true)
    private Integer utenteId;

    @NotBlank(message = "Il tipo documento è obbligatorio")
    @Size(max = 50, message = "Il tipo documento non può superare i 50 caratteri")
    @Schema(description = "Tipo di documento", example = "CARTA_IDENTITA", required = true)
    private String tipoDocumento;

    @NotBlank(message = "Il numero documento è obbligatorio")
    @Size(max = 50, message = "Il numero documento non può superare i 50 caratteri")
    @Schema(description = "Numero del documento", example = "AB1234567", required = true)
    private String numeroDocumento;

    @Schema(description = "Data di rilascio del documento", example = "2020-01-15")
    private LocalDate dataRilascio;

    @Schema(description = "Data di scadenza del documento", example = "2030-01-15")
    private LocalDate dataScadenza;

    @Size(max = 100, message = "L'ente rilasciante non può superare i 100 caratteri")
    @Schema(description = "Ente che ha rilasciato il documento", example = "Comune di Milano")
    private String rilasciatoDa;

    @Schema(description = "Data di creazione del record", example = "2024-01-15T10:30:00")
    private LocalDateTime dataCreazione;
}
