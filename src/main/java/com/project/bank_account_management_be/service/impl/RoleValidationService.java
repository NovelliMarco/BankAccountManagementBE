package com.project.bank_account_management_be.service;

import com.project.bank_account_management_be.entity.Utente;
import com.project.bank_account_management_be.error.UtenteNotFoundException;
import com.project.bank_account_management_be.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleValidationService {

    private final UtenteRepository utenteRepository;

    private static final List<String> RUOLI_OPERATORI = Arrays.asList("AMMINISTRATORE", "OPERATORE");
    private static final List<String> RUOLI_ADMIN = Arrays.asList("AMMINISTRATORE");

    /**
     * Verifica che l'operatore abbia i permessi per operare sui conti
     */
    public void validateOperatorePermissions(Integer operatoreId) {
        Utente operatore = utenteRepository.findById(operatoreId)
                .orElseThrow(() -> new UtenteNotFoundException("Operatore non trovato con ID: " + operatoreId));

        String ruoloOperatore = getRuoloUtente(operatore);

        if (!RUOLI_OPERATORI.contains(ruoloOperatore)) {
            throw new SecurityException("L'utente non ha i permessi necessari per questa operazione. Ruolo richiesto: AMMINISTRATORE o OPERATORE");
        }

        log.info("Operatore validato: {} - Ruolo: {}", operatore.getCodiceFiscale(), ruoloOperatore);
    }

    /**
     * Verifica che l'operatore abbia i permessi di amministratore
     */
    public void validateAdminPermissions(Integer operatoreId) {
        Utente operatore = utenteRepository.findById(operatoreId)
                .orElseThrow(() -> new UtenteNotFoundException("Operatore non trovato con ID: " + operatoreId));

        String ruoloOperatore = getRuoloUtente(operatore);

        if (!RUOLI_ADMIN.contains(ruoloOperatore)) {
            throw new SecurityException("L'utente non ha i permessi di amministratore per questa operazione");
        }

        log.info("Amministratore validato: {} - Ruolo: {}", operatore.getCodiceFiscale(), ruoloOperatore);
    }

    /**
     * Verifica che l'operatore possa creare utenti
     */
    public void validateUserCreationPermissions(Integer operatoreId) {
        validateOperatorePermissions(operatoreId); // Usa gli stessi permessi degli operatori
    }

    /**
     * Ottiene il ruolo principale dell'utente
     */
    public String getRuoloUtente(Utente utente) {
        // Prima controlla il ruolo singolo (per compatibilità)
        if (utente.getRuolo() != null) {
            return utente.getRuolo().getNomeRuolo();
        }

        // Poi controlla i ruoli multipli
        if (utente.getRuoli() != null && !utente.getRuoli().isEmpty()) {
            // Priorità: AMMINISTRATORE > OPERATORE > altri ruoli
            for (String ruoloPrioritario : RUOLI_ADMIN) {
                if (utente.getRuoli().stream().anyMatch(r -> ruoloPrioritario.equals(r.getNomeRuolo()))) {
                    return ruoloPrioritario;
                }
            }
            for (String ruoloOperatore : RUOLI_OPERATORI) {
                if (utente.getRuoli().stream().anyMatch(r -> ruoloOperatore.equals(r.getNomeRuolo()))) {
                    return ruoloOperatore;
                }
            }
            // Restituisce il primo ruolo se nessuno ha priorità
            return utente.getRuoli().get(0).getNomeRuolo();
        }

        return "CLIENTE"; // Ruolo di default
    }

    /**
     * Verifica se l'utente ha un ruolo specifico
     */
    public boolean hasRole(Utente utente, String ruolo) {
        String ruoloUtente = getRuoloUtente(utente);
        return ruolo.equals(ruoloUtente);
    }

    /**
     * Verifica se l'utente ha uno dei ruoli specificati
     */
    public boolean hasAnyRole(Utente utente, String... ruoli) {
        String ruoloUtente = getRuoloUtente(utente);
        return Arrays.asList(ruoli).contains(ruoloUtente);
    }

    /**
     * Verifica se l'operatore può operare su un utente target
     * Gli operatori possono operare solo su clienti, gli admin su tutti
     */
    public void validateTargetUserOperation(Integer operatoreId, Integer targetUserId) {
        Utente operatore = utenteRepository.findById(operatoreId)
                .orElseThrow(() -> new UtenteNotFoundException("Operatore non trovato con ID: " + operatoreId));

        Utente targetUser = utenteRepository.findById(targetUserId)
                .orElseThrow(() -> new UtenteNotFoundException("Utente target non trovato con ID: " + targetUserId));

        String ruoloOperatore = getRuoloUtente(operatore);
        String ruoloTarget = getRuoloUtente(targetUser);

        // Gli amministratori possono operare su tutti
        if (RUOLI_ADMIN.contains(ruoloOperatore)) {
            return;
        }

        // Gli operatori possono operare solo su clienti
        if (RUOLI_OPERATORI.contains(ruoloOperatore) && "CLIENTE".equals(ruoloTarget)) {
            return;
        }

        throw new SecurityException("L'operatore non ha i permessi per operare su questo utente");
    }

    /**
     * Ottiene l'utente operatore validato
     */
    public Utente getValidatedOperatore(Integer operatoreId) {
        Utente operatore = utenteRepository.findById(operatoreId)
                .orElseThrow(() -> new UtenteNotFoundException("Operatore non trovato con ID: " + operatoreId));

        validateOperatorePermissions(operatoreId);
        return operatore;
    }
}