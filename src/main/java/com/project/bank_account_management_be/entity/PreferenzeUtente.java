package com.project.bank_account_management_be.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "preferenze_utente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "utente")
@ToString(exclude = "utente")
public class PreferenzeUtente {

    @Id
    @Column(name = "UtenteID")
    private Integer utenteId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // usa la stessa chiave primaria dell'utente
    @JoinColumn(name = "UtenteID")
    private Utente utente;

    @Column(name = "Lingua", length = 10)
    private String lingua;

    @Column(name = "ValutaPreferita", length = 10)
    private String valutaPreferita;

    @Column(name = "NotificheEmail")
    private Boolean notificheEmail;

    @Column(name = "NotificheSMS")
    private Boolean notificheSMS;
}
