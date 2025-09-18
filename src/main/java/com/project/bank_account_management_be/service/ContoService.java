package com.project.bank_account_management_be.service;

import com.project.bank_account_management_be.dto.BonificoDTO;
import com.project.bank_account_management_be.dto.ContoDTO;
import com.project.bank_account_management_be.dto.StoricoMovimentoContoDTO;
import com.project.bank_account_management_be.dto.TransazioneContoDTO;
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
}