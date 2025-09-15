package com.project.bank_account_management_be.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "StoricoMovimentoCarta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "movimentoId")
@ToString(exclude = {"carta", "tipologiaMovimento"})
public class StoricoMovimentoCarta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MovimentoID")
    private Integer movimentoID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CartaID", nullable = false)
    private Carta carta;

    @CreatedDate
    @Column(name = "DataMovimento", nullable = false, updatable = false)
    private LocalDateTime dataMovimento;

    @Column(name = "Importo", nullable = false, precision = 18, scale = 2)
    private BigDecimal importo;

    @ManyToOne
    @JoinColumn(name = "TipoMovimentoID", nullable = false)
    private TipologiaMovimento tipoMovimento;

    @Column(name = "Esercente")
    private String esercente;

    @Column(name = "Localita")
    private String localita;

    @Column(name = "Descrizione")
    private String descrizione;

    @OneToMany(mappedBy = "carta", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<StoricoMovimentoCarta> movimenti = new ArrayList<>();
}

