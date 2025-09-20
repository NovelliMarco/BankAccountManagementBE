package com.project.bank_account_management_be.service;

import com.project.bank_account_management_be.dto.CreateUtenteDTO;
import com.project.bank_account_management_be.dto.DeleteUtenteDTO;
import com.project.bank_account_management_be.dto.UtenteDTO;
import com.project.bank_account_management_be.entity.Utente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UtenteService {

    Page<UtenteDTO> getAllUtenti(Pageable pageable);

    UtenteDTO getUtenteById(Integer id);

    UtenteDTO getUtenteByCodiceFiscale(String codiceFiscale);

    UtenteDTO createUtente(CreateUtenteDTO dto);

    UtenteDTO updateUtente(Integer id, UtenteDTO dto);

    void deleteUtente(Integer id, DeleteUtenteDTO deleteDTO);

    List<UtenteDTO> searchUtenti(String nome, String cognome);

    boolean emailExists(String email);

    boolean codiceFiscaleExists(String codiceFiscale);

    // Aggiungi questi metodi all'interfaccia UtenteService esistente

    /**
     * Crea un utente da parte di un operatore
     */
    UtenteDTO createUtenteByOperator(CreateUtenteDTO dto, Integer operatoreId);

    /**
     * Aggiorna un utente da parte di un operatore
     */
    UtenteDTO updateUtenteByOperator(Integer id, UtenteDTO dto, Integer operatoreId);

    /**
     * Ottiene tutti gli utenti per gli amministratori (senza paginazione)
     */
    List<UtenteDTO> getAllUtentiForAdmin();

    /**
     * Ottiene statistiche sugli utenti per gli amministratori
     */
    Object getStatisticheUtenti();

    /**
     * Valida se un utente può essere creato
     */
    void validateUtenteCreation(CreateUtenteDTO dto);

    /**
     * Assegna ruolo di default al nuovo utente
     */
    void assignDefaultRole(Utente utente);
}
