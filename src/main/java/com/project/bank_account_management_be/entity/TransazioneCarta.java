package com.project.bank_account_management_be.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TransazioneCarta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "transazioneId")
@ToString(exclude = {"carta", "tipologiaTransazione"})
public class TransazioneCarta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransazioneID")
    private Integer transazioneId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CartaID", nullable = false)
    private Carta carta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TipoTransazioneID", nullable = false)
    private TipologiaTransazione tipologiaTransazione;

    @CreatedDate
    @Column(name = "DataOperazione", nullable = false, updatable = false)
    private LocalDateTime dataOperazione;

    @Column(name = "Importo", nullable = false, precision = 18, scale = 2)
    private BigDecimal importo;

    @Column(name = "Esercente")
    private String esercente;

    @Column(name = "Localita", length = 100)
    private String localita;

    @Column(name = "StatoTransazione", length = 20)
    @Builder.Default
    private String statoTransazione = "COMPLETATA";
}
