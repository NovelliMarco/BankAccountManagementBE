package com.project.bank_account_management_be.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "documentoidentita")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "documentoId")
@ToString(exclude = "utente")
public class DocumentoIdentita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DocumentoID")
    private Integer documentoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UtenteID", nullable = false)
    private Utente utente;

    @Column(name = "TipoDocumento", nullable = false, length = 50)
    private String tipoDocumento;

    @Column(name = "NumeroDocumento", nullable = false, length = 50)
    private String numeroDocumento;

    @Column(name = "DataRilascio")
    private LocalDate dataRilascio;

    @Column(name = "DataScadenza")
    private LocalDate dataScadenza;

    @Column(name = "RilasciatoDa", length = 100)
    private String rilasciatoDa;

    @CreatedDate
    @Column(name = "DataCreazione", updatable = false)
    private LocalDateTime dataCreazione;
}
