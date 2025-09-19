package com.project.bank_account_management_be.service.impl;

import com.project.bank_account_management_be.entity.Login;
import com.project.bank_account_management_be.entity.Utente;
import com.project.bank_account_management_be.error.UtenteNotFoundException;
import com.project.bank_account_management_be.repository.LoginRepository;
import com.project.bank_account_management_be.repository.UtenteRepository;
import com.project.bank_account_management_be.service.LoginService;
import com.project.bank_account_management_be.util.CryptoUtil;
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
    private final CryptoUtil cryptoUtil;

    @Override
    public boolean authenticate(String email, String hashedPassword) {
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

            // Il frontend passa già la password crittografata, confronta direttamente
            boolean passwordMatch = hashedPassword.equals(login.getPasswordHash());

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

            // Genera una password temporanea e la cripta
            String passwordTemporanea = generaPasswordTemporanea();
            String passwordHash = cryptoUtil.hashPassword(passwordTemporanea);

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

            // Restituisce la password temporanea decriptata (in chiaro)
            return "Una nuova password temporanea è stata generata: " + passwordTemporanea +
                    ". Ti consigliamo di cambiarla al prossimo accesso. " +
                    "Hash SHA-256: " + passwordHash;

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
        // Genera una password temporanea di 12 caratteri
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        StringBuilder password = new StringBuilder();
        java.util.Random random = new java.util.Random();

        for (int i = 0; i < 12; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }
}