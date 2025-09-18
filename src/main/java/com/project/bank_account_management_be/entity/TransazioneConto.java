package com.project.bank_account_management_be.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TransazioneConto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "transazioneId")
@ToString(exclude = {"conto", "tipologiaTransazione"})
public class TransazioneConto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransazioneID")
    private Integer transazioneId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ContoID", nullable = false)
    private Conto conto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TipoTransazioneID", nullable = false)
    private TipologiaTransazione tipologiaTransazione;

    @CreatedDate
    @Column(name = "DataOperazione", nullable = false, updatable = false)
    private LocalDateTime dataOperazione;

    @Column(name = "Importo", nullable = false, precision = 18, scale = 2)
    private BigDecimal importo;

    @Column(name = "Descrizione")
    private String descrizione;

    @Column(name = "StatoTransazione", length = 20)
    @Builder.Default
    private String statoTransazione = "COMPLETATA";
}
