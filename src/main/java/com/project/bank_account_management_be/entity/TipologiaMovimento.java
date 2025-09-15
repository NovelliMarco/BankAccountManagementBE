package com.project.bank_account_management_be.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TipologiaMovimento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "tipoMovimentoId")
@ToString(exclude = "movimenti")
public class TipologiaMovimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TipoMovimentoID")
    private Integer tipoMovimentoId;

    @Column(name = "Codice", unique = true, nullable = false, length = 30)
    private String codice;

    @Column(name = "Descrizione", nullable = false)
    private String descrizione;

    @Enumerated(EnumType.STRING)
    @Column(name = "Direzione", nullable = false)
    private DirezioneMovimento direzione;

    @OneToMany(mappedBy = "tipologiaMovimento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<StoricoMovimentoConto> movimenti = new ArrayList<>();

    @CreatedDate
    @Column(name = "DataCreazione", updatable = false)
    private LocalDateTime dataCreazione;
}
