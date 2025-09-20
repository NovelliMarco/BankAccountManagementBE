package com.project.bank_account_management_be.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "utente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "utenteId")
@ToString(exclude = {"login", "ruolo", "documenti", "conti", "carte", "accessLog", "preferenze"})
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UtenteID")
    private Integer utenteId;

    @Column(name = "Nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "Cognome", nullable = false, length = 100)
    private String cognome;

    @Column(name = "DataNascita")
    private LocalDate dataNascita;

    @Column(name = "Professione", length = 100)
    private String professione;

    @Column(name = "CodiceFiscale", unique = true, nullable = false, length = 16)
    private String codiceFiscale;

    @Column(name = "Indirizzo")
    private String indirizzo;

    @Column(name = "Citta", length = 100)
    private String citta;

    @Column(name = "CAP", length = 10)
    private String cap;

    @Column(name = "Provincia", length = 10)
    private String provincia;

    @Column(name = "Nazione", length = 100)
    private String nazione;

    @Column(name = "Telefono", length = 20)
    private String telefono;

    @Column(name = "Email", unique = true, nullable = false)
    private String email;

    @CreatedDate
    @Column(name = "DataCreazione", updatable = false)
    private LocalDateTime dataCreazione;

    // Relazione con Login
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "LoginID", unique = true)
    private Login login;

    // Relazione con Ruolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RuoloID")
    private Ruolo ruolo;

    // Documenti di identità
    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<DocumentoIdentita> documenti = new ArrayList<>();

    // Conti bancari
    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<Conto> conti = new ArrayList<>();

    // Carte
    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<Carta> carte = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "utente_ruolo",
            joinColumns = @JoinColumn(name = "UtenteID"),
            inverseJoinColumns = @JoinColumn(name = "RuoloID"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"UtenteID", "RuoloID"})
    )
    @Builder.Default
    private List<Ruolo> ruoli = new ArrayList<>();
}
