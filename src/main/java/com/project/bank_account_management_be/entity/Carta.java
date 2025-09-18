package com.project.bank_account_management_be.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Carta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "cartaId")
@ToString(exclude = {"utente", "transazioni"})
public class Carta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CartaID")
    private Integer cartaId;

    @Column(name = "NumeroCarta", unique = true, nullable = false, length = 16)
    private String numeroCarta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UtenteID", nullable = false)
    private Utente utente;

    @Column(name = "TipoCarta", nullable = false, length = 20)
    private String tipoCarta;

    @Column(name = "DataScadenza", nullable = false)
    private LocalDate dataScadenza;

    @Column(name = "CVV", nullable = false, length = 3)
    private String cvv;

    @Column(name = "LimiteGiornaliero", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal limiteGiornaliero = new BigDecimal("500.00");

    @Column(name = "LimiteMensile", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal limiteMensile = new BigDecimal("3000.00");

    @Column(name = "LimiteAnnuale", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal limiteAnnuale = new BigDecimal("30000.00");

    @Column(name = "Bloccata", nullable = false)
    @Builder.Default
    private Boolean bloccata = false;

    @Column(name = "DataBlocco")
    private LocalDateTime dataBlocco;

    @Column(name = "Stato", nullable = false, length = 20)
    @Builder.Default
    private String stato = "ATTIVA";

    @OneToMany(mappedBy = "carta", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<TransazioneCarta> transazioni = new ArrayList<>();

    @CreatedDate
    @Column(name = "DataCreazione", updatable = false)
    private LocalDateTime dataCreazione;
}

