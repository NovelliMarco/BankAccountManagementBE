package com.project.bank_account_management_be.dto;

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
public class TransazioneCartaDTO {
    private Integer id;
    private BigDecimal importo;
    private String esercente;
    private String localita;
    private String statoTransazione;
    private String tipoTransazione; // Nome della tipologia
    private LocalDateTime dataOperazione;
}
