package com.project.bank_account_management_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO per lo storico dei movimenti del conto")
public class StoricoMovimentoContoDTO {

    @Schema(description = "ID univoco del movimento", example = "1")
    private Integer id;

    @Schema(description = "Data e ora del movimento", example = "2024-01-15T14:30:00")
    private LocalDateTime dataMovimento;

    @Schema(description = "Importo del movimento", example = "250.00")
    private BigDecimal importo;

    @Schema(description = "Tipo di movimento", example = "BONIFICO_IN")
    private String tipoMovimento;

    @Schema(description = "Direzione del movimento", example = "ENTRATA")
    private String direzioneMovimento;

    @Schema(description = "Descrizione del movimento", example = "Bonifico ricevuto da Giovanni Bianchi")
    private String descrizione;

    @Schema(description = "Saldo dopo il movimento", example = "1750.50")
    private BigDecimal saldoDopoMovimento;
}