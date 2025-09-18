package com.project.bank_account_management_be.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "contoId")
@ToString(exclude = {"utente", "transazioni", "movimenti"})
public class Conto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ContoID")
    private Integer contoId;

    @Column(name = "IBAN", unique = true, nullable = false, length = 34)
    private String iban;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UtenteID", nullable = false)
    private Utente utente;

    @Column(name = "TipoConto", nullable = false, length = 100)
    private String tipoConto;

    @Column(name = "SaldoDisponibile", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal saldoDisponibile = BigDecimal.ZERO;

    @Column(name = "SaldoContabile", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal saldoContabile = BigDecimal.ZERO;

    @CreatedDate
    @Column(name = "DataApertura", nullable = false, updatable = false)
    private LocalDateTime dataApertura;

    @Column(name = "Stato", nullable = false, length = 20)
    @Builder.Default
    private String stato = "ATTIVO";

    @Column(name = "ContoAperto", nullable = false)
    @Builder.Default
    private Boolean contoAperto = true;

    @OneToMany(mappedBy = "conto", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<TransazioneConto> transazioni = new ArrayList<>();

    @OneToMany(mappedBy = "conto", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<StoricoMovimentoConto> movimenti = new ArrayList<>();
}

