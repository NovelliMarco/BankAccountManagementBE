package com.project.bank_account_management_be.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "access_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "logId")
@ToString(exclude = "utente")
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LogID")
    private Integer logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UtenteID", nullable = false)
    private Utente utente;

    @Column(name = "DataAccesso", nullable = false)
    private LocalDateTime dataAccesso;

    @Column(name = "IndirizzoIP", length = 45)
    private String indirizzoIP;

    @Column(name = "UserAgent", columnDefinition = "TEXT")
    private String userAgent;
}
