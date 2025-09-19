package com.project.bank_account_management_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO per le tipologie di movimento")
public class TipologiaMovimentoDTO {

    @Schema(description = "ID univoco della tipologia", example = "1")
    private Integer id;

    @NotBlank(message = "Il codice è obbligatorio")
    @Size(max = 30, message = "Il codice non può superare i 30 caratteri")
    @Schema(description = "Codice della tipologia", example = "DEPOSITO", required = true)
    private String codice;

    @NotBlank(message = "La descrizione è obbligatoria")
    @Schema(description = "Descrizione della tipologia", example = "Deposito contanti", required = true)
    private String descrizione;

    @NotNull(message = "La direzione è obbligatoria")
    @Schema(description = "Direzione del movimento", example = "ENTRATA", required = true)
    private DirezioneMovimento direzione;

    @Schema(description = "Data di creazione", example = "2024-01-15T10:30:00")
    private LocalDateTime dataCreazione;
}