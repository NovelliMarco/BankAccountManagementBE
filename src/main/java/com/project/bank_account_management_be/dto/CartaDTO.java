package com.project.bank_account_management_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartaDTO {
    private Integer id;
    private String numeroCarta;
    private String tipoCarta;
    private LocalDate dataScadenza;
    private BigDecimal limiteGiornaliero;
    private BigDecimal limiteMensile;
    private BigDecimal limiteAnnuale;
    private Boolean bloccata;
    private String stato;
    private LocalDateTime dataCreazione;
}
