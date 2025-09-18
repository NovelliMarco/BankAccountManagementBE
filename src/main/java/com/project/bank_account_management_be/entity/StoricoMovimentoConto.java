package com.project.bank_account_management_be.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "StoricoMovimentoConto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "movimentoId")
@ToString(exclude = {"conto", "tipologiaMovimento"})
public class StoricoMovimentoConto {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MovimentoID")
    private Integer movimentoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ContoID", nullable = false)
    private Conto conto;

    @CreatedDate
    @Column(name = "DataMovimento", nullable = false, updatable = false)
    private LocalDateTime dataMovimento;

    @Column(name = "Importo", nullable = false, precision = 18, scale = 2)
    private BigDecimal importo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TipoMovimentoID", nullable = false)
    private TipologiaMovimento tipologiaMovimento;

    @Column(name = "Descrizione")
    private String descrizione;

    @Column(name = "SaldoDopoMovimento", precision = 18, scale = 2)
    private BigDecimal saldoDopoMovimento;
}

