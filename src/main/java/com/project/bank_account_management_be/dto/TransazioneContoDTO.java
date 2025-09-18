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
@Schema(description = "DTO per le transazioni del conto bancario")
public class TransazioneContoDTO {

    @Schema(description = "ID univoco della transazione", example = "1")
    private Integer id;

    @Schema(description = "Importo della transazione", example = "250.00")
    private BigDecimal importo;

    @Schema(description = "Descrizione della transazione", example = "Bonifico a Mario Rossi")
    private String descrizione;

    @Schema(description = "Stato della transazione", example = "COMPLETATA")
    private String statoTransazione;

    @Schema(description = "Tipo di transazione", example = "BONIFICO_OUT")
    private String tipoTransazione;

    @Schema(description = "Data e ora dell'operazione", example = "2024-01-15T14:30:00")
    private LocalDateTime dataOperazione;
}