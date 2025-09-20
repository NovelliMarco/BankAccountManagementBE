package com.project.bank_account_management_be.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "auditId")
@ToString(exclude = {"operatore", "utenteTarget"})
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AuditID")
    private Long auditId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OperatoreID", nullable = false)
    private Utente operatore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UtenteTargetID")
    private Utente utenteTarget;

    @Column(name = "TipoOperazione", nullable = false, length = 100)
    private String tipoOperazione;

    @Column(name = "Descrizione", columnDefinition = "TEXT")
    private String descrizione;

    @Column(name = "EntityId")
    private Integer entityId;

    @Column(name = "EntityType", length = 50)
    private String entityType;

    @CreatedDate
    @Column(name = "DataOperazione", nullable = false, updatable = false)
    private LocalDateTime dataOperazione;

    @Column(name = "IndirizzoIP", length = 45)
    private String indirizzoIP;

    @Column(name = "UserAgent", length = 500)
    private String userAgent;

    @Column(name = "EsitoOperazione", nullable = false)
    @Builder.Default
    private Boolean esitoOperazione = true;

    @Column(name = "DettagliErrore", columnDefinition = "TEXT")
    private String dettagliErrore;

    // Campi per tracciare i valori prima e dopo le modifiche
    @Column(name = "ValoriPrecedenti", columnDefinition = "JSON")
    private String valoriPrecedenti;

    @Column(name = "NuoviValori", columnDefinition = "JSON")
    private String nuoviValori;

    @PrePersist
    protected void onCreate() {
        dataOperazione = LocalDateTime.now();
    }
}