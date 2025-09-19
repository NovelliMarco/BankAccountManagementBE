package com.project.bank_account_management_be.service;

public interface LoginService {

    /**
     * Autentica un utente con email e password
     * @param email Email dell'utente
     * @param password Password dell'utente
     * @return true se l'autenticazione è riuscita, false altrimenti
     */
    boolean authenticate(String email, String password);

    /**
     * Avvia la procedura di recupero password
     * @param nome Nome dell'utente
     * @param cognome Cognome dell'utente
     * @param codiceFiscale Codice fiscale dell'utente
     * @param email Email dell'utente
     * @return Messaggio di conferma o errore
     */
    String recuperoPassword(String nome, String cognome, String codiceFiscale, String email);
}