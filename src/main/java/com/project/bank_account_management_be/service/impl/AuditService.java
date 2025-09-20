package com.project.bank_account_management_be.service.impl;

import com.project.bank_account_management_be.entity.AuditLog;
import com.project.bank_account_management_be.entity.Utente;
import com.project.bank_account_management_be.repository.AuditLogRepository;
import com.project.bank_account_management_be.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final UtenteRepository utenteRepository;
    private final HttpServletRequest request;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logOperazioneUtente(Integer operatoreId, String tipoOperazione, String descrizione,
                                    Integer utenteTargetId, String entityType, Integer entityId) {
        try {
            Utente operatore = utenteRepository.findById(operatoreId)
                    .orElseThrow(() -> new RuntimeException("Operatore non trovato"));

            Utente utenteTarget = null;
            if (utenteTargetId != null) {
                utenteTarget = utenteRepository.findById(utenteTargetId).orElse(null);
            }

            AuditLog auditLog = AuditLog.builder()
                    .operatore(operatore)
                    .utenteTarget(utenteTarget)
                    .tipoOperazione(tipoOperazione)
                    .descrizione(descrizione)
                    .entityId(entityId)
                    .entityType(entityType)
                    .dataOperazione(LocalDateTime.now())
                    .indirizzoIP(getClientIP())
                    .userAgent(getUserAgent())
                    .esitoOperazione(true)
                    .build();

            auditLogRepository.save(auditLog);
            log.info("Audit log salvato: {} - Operatore: {} - Target: {}",
                    tipoOperazione, operatore.getCodiceFiscale(),
                    utenteTarget != null ? utenteTarget.getCodiceFiscale() : "N/A");

        } catch (Exception e) {
            log.error("Errore nel salvataggio dell'audit log", e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logOperazioneConErrore(Integer operatoreId, String tipoOperazione, String descrizione,
                                       String dettagliErrore, String entityType, Integer entityId) {
        try {
            Utente operatore = utenteRepository.findById(operatoreId)
                    .orElseThrow(() -> new RuntimeException("Operatore non trovato"));

            AuditLog auditLog = AuditLog.builder()
                    .operatore(operatore)
                    .tipoOperazione(tipoOperazione)
                    .descrizione(descrizione)
                    .entityId(entityId)
                    .entityType(entityType)
                    .dataOperazione(LocalDateTime.now())
                    .indirizzoIP(getClientIP())
                    .userAgent(getUserAgent())
                    .esitoOperazione(false)
                    .dettagliErrore(dettagliErrore)
                    .build();

            auditLogRepository.save(auditLog);
            log.warn("Audit log errore salvato: {} - Operatore: {}",
                    tipoOperazione, operatore.getCodiceFiscale());

        } catch (Exception e) {
            log.error("Errore nel salvataggio dell'audit log di errore", e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logCreazioneUtente(Integer operatoreId, Integer nuovoUtenteId, String codiceFiscaleNuovoUtente) {
        logOperazioneUtente(operatoreId, "CREAZIONE_UTENTE",
                "Creato nuovo utente: " + codiceFiscaleNuovoUtente,
                nuovoUtenteId, "UTENTE", nuovoUtenteId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logCreazioneConto(Integer operatoreId, Integer contoId, String ibanConto,
                                  Integer utenteTargetId, String tipoConto) {
        logOperazioneUtente(operatoreId, "CREAZIONE_CONTO",
                String.format("Creato nuovo conto %s - IBAN: %s", tipoConto, ibanConto),
                utenteTargetId, "CONTO", contoId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logChiusuraConto(Integer operatoreId, Integer contoId, String ibanConto,
                                 Integer utenteTargetId, String motivo) {
        logOperazioneUtente(operatoreId, "CHIUSURA_CONTO",
                String.format("Chiuso conto IBAN: %s - Motivo: %s", ibanConto, motivo),
                utenteTargetId, "CONTO", contoId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logRiaperturaConto(Integer operatoreId, Integer contoId, String ibanConto,
                                   Integer utenteTargetId, String motivo) {
        logOperazioneUtente(operatoreId, "RIAPERTURA_CONTO",
                String.format("Riaperto conto IBAN: %s - Motivo: %s", ibanConto, motivo),
                utenteTargetId, "CONTO", contoId);
    }

    private String getClientIP() {
        try {
            String xForwardedFor = request.getHeader("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                return xForwardedFor.split(",")[0].trim();
            }

            String xRealIP = request.getHeader("X-Real-IP");
            if (xRealIP != null && !xRealIP.isEmpty()) {
                return xRealIP;
            }

            return request.getRemoteAddr();
        } catch (Exception e) {
            return "unknown";
        }
    }

    private String getUserAgent() {
        try {
            return request.getHeader("User-Agent");
        } catch (Exception e) {
            return "unknown";
        }
    }
}