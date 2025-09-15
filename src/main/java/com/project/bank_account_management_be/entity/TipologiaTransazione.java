package com.project.bank_account_management_be.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TipologiaTransazione")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "tipoTransazioneId")
@ToString(exclude = {"transazioniConto", "transazioniCarta"})
public class TipologiaTransazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TipoTransazioneID")
    private Integer tipoTransazioneId;

    @Column(name = "Codice", unique = true, nullable = false, length = 30)
    private String codice;

    @Column(name = "Descrizione", nullable = false)
    private String descrizione;

    @OneToMany(mappedBy = "tipologiaTransazione", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TransazioneConto> transazioniConto = new ArrayList<>();

    @OneToMany(mappedBy = "tipologiaTransazione", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TransazioneCarta> transazioniCarta = new ArrayList<>();

    @CreatedDate
    @Column(name = "DataCreazione", updatable = false)
    private LocalDateTime dataCreazione;
}
