package com.project.bank_account_management_be.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ruolo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "ruoloId")
@ToString(exclude = "utenti")
public class Ruolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RuoloID")
    private Integer ruoloId;

    @Column(name = "NomeRuolo", nullable = false, unique = true, length = 50)
    private String nomeRuolo;

    @Column(name = "Descrizione", length = 255)
    private String descrizione;

    @ManyToMany(mappedBy = "ruoli", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Utente> utenti = new ArrayList<>();
}
