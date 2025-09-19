package com.project.bank_account_management_be.service.impl;

import com.project.bank_account_management_be.entity.Login;
import com.project.bank_account_management_be.entity.Utente;
import com.project.bank_account_management_be.error.UtenteNotFoundException;
import com.project.bank_account_management_be.repository.LoginRepository;
import com.project.bank_account_management_be.repository.UtenteRepository;
import com.project.bank_account_management_be.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LoginServiceImpl implements LoginService {

    private final UtenteRepository utenteRepository;
    private final LoginRepository loginRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public boolean authenticate(String email, String password) {
        try {
            Optional<Utente> utenteOpt = utenteRepository.findByEmail(email);
            if (utenteOpt.isEmpty()) {
                log.warn("Tentativo di login con email non esistente: {}", email);
                return false;
            }

            Utente utente = utenteOpt.get();
            Login login = utente.getLogin();

            if (login == null || login.getPasswordHash() == null || login.getPasswordHash().isEmpty()) {
                log.warn("Utente {} non ha una password impostata", email);
                return false;
            }

            // Verifica la password
            boolean passwordMatch = passwordEncoder.matches(password, login.getPasswordHash());

            if (passwordMatch) {
                // Aggiorna ultimo accesso
                login.setUltimoAccesso(LocalDateTime.now());
                loginRepository.save(login);
                log.info("Login riuscito per utente: {}", email);
                return true;
            } else {
                log.warn("Password errata per utente: {}", email);
                return false;
            }

        } catch (Exception e) {
            log.error("Errore durante l'autenticazione per email: {}", email, e);
            return false;
        }
    }

    @Override
    public String recuperoPassword(String nome, String cognome, String codiceFiscale, String email) {
        try {
            // Verifica che l'utente esista con tutti i dati forniti
            Optional<Utente> utenteOpt = utenteRepository.findByCodiceFiscale(codiceFiscale);

            if (utenteOpt.isEmpty()) {
                throw new UtenteNotFoundException("Utente non trovato con i dati forniti");
            }

            Utente utente = utenteOpt.get();

            // Verifica che tutti i dati corrispondano
            if (!utente.getNome().equalsIgnoreCase(nome) ||
                    !utente.getCognome().equalsIgnoreCase(cognome) ||
                    !utente.getEmail().equalsIgnoreCase(email)) {

                log.warn("Tentativo di recupero password con dati non corrispondenti per CF: {}", codiceFiscale);
                throw new UtenteNotFoundException("I dati forniti non corrispondono a nessun utente");
            }

            // Genera una password temporanea
            String passwordTemporanea = generaPasswordTemporanea();
            String passwordHash = passwordEncoder.encode(passwordTemporanea);

            // Aggiorna la password nel database
            Login login = utente.getLogin();
            if (login == null) {
                // Crea un nuovo login se non esiste
                login = Login.builder()
                        .email(email)
                        .passwordHash(passwordHash)
                        .build();
                utente.setLogin(login);
            } else {
                login.setPasswordHash(passwordHash);
            }

            utenteRepository.save(utente);

            log.info("Password temporanea generata per utente: {} - CF: {}", email, codiceFiscale);

            // In un sistema reale, qui invieresti un'email con la password temporanea
            // Per ora restituiamo un messaggio generico
            return "Una nuova password temporanea è stata generata e sarà inviata all'indirizzo email associato al tuo account. " +
                    "Password temporanea: " + passwordTemporanea + " (in produzione sarebbe inviata via email)";

        } catch (UtenteNotFoundException e) {
            log.warn("Tentativo di recupero password per utente non esistente: {} {} - CF: {}",
                    nome, cognome, codiceFiscale);
            throw e;
        } catch (Exception e) {
            log.error("Errore durante il recupero password per CF: {}", codiceFiscale, e);
            throw new RuntimeException("Errore durante il recupero password. Riprovare più tardi.");
        }
    }

    private String generaPasswordTemporanea() {
        // Genera una password temporanea sicura
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return "Temp" + uuid.substring(0, 8) + "!";
    }
}