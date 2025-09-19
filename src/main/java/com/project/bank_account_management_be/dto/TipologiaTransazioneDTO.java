package com.project.bank_account_management_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO per le tipologie di transazione")
public class TipologiaTransazioneDTO {

    @Schema(description = "ID univoco della tipologia", example = "1")
    private Integer id;

    @NotBlank(message = "Il codice è obbligatorio")
    @Size(max = 30, message = "Il codice non può superare i 30 caratteri")
    @Schema(description = "Codice della tipologia", example = "BONIFICO", required = true)
    private String codice;

    @NotBlank(message = "La descrizione è obbligatoria")
    @Schema(description = "Descrizione della tipologia", example = "Bonifico bancario", required = true)
    private String descrizione;

    @Schema(description = "Data di creazione", example = "2024-01-15T10:30:00")
    private LocalDateTime dataCreazione;
}
