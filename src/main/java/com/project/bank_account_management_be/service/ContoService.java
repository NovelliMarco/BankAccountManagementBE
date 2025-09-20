package com.project.bank_account_management_be.service;

import com.project.bank_account_management_be.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ContoService {

    List<ContoDTO> getAllConti();

    ContoDTO getContoById(Integer id);

    ContoDTO getContoByIban(String iban);

    ContoDTO createConto(ContoDTO dto);

    ContoDTO updateConto(Integer id, ContoDTO dto);

    void deleteConto(Integer id);

    ContoDTO chiudiConto(Integer id);

    ContoDTO riapriConto(Integer id);

    ContoDTO deposito(Integer id, BigDecimal importo, String descrizione);

    ContoDTO prelievo(Integer id, BigDecimal importo, String descrizione);

    void bonifico(Integer contoMittenteId, BonificoDTO bonifico);

    List<ContoDTO> getContiByCodiceFiscale(String codiceFiscale);

    List<ContoDTO> getContiByNomeECognome(String nome, String cognome);

    Page<TransazioneContoDTO> getTransazioniConto(Integer contoId, Pageable pageable);

    Page<StoricoMovimentoContoDTO> getMovimentiConto(Integer contoId, Pageable pageable);

    boolean ibanExists(String iban);

    /**
     * Crea un conto per un utente da parte di un operatore
     */
    ContoDTO createContoByOperator(CreateContoDTO dto, Integer operatoreId);

    /**
     * Chiude un conto da parte di un operatore
     */
    ContoDTO chiudiContoByOperator(Integer contoId, Integer operatoreId, String motivo);

    /**
     * Riapre un conto da parte di un operatore
     */
    ContoDTO riapriContoByOperator(Integer contoId, Integer operatoreId, String motivo);

    /**
     * Effettua un deposito forzato da parte di un amministratore
     */
    ContoDTO depositoForzatoByAdmin(Integer contoId, BigDecimal importo, String motivo, Integer adminId);

    /**
     * Ottiene statistiche sui conti per gli amministratori
     */
    Object getStatisticheConti();

    /**
     * Genera un IBAN univoco per un nuovo conto
     */
    String generateUniqueIban();

    /**
     * Valida se un conto può essere creato per l'utente
     */
    void validateContoCreation(String codiceFiscale, String tipoConto);
}