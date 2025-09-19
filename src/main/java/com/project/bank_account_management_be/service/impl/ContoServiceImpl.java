package com.project.bank_account_management_be.service.impl;

import com.project.bank_account_management_be.dto.*;
import com.project.bank_account_management_be.entity.*;
import com.project.bank_account_management_be.error.ContoChiusoException;
import com.project.bank_account_management_be.error.ContoNotFoundException;
import com.project.bank_account_management_be.error.SaldoInsufficienteException;
import com.project.bank_account_management_be.mapper.ContoMapper;
import com.project.bank_account_management_be.mapper.StoricoMovimentoContoMapper;
import com.project.bank_account_management_be.mapper.TransazioneContoMapper;
import com.project.bank_account_management_be.repository.*;
import com.project.bank_account_management_be.service.ContoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ContoServiceImpl implements ContoService {

    private final ContoRepository contoRepository;
    private final ContoMapper contoMapper;

    private final UtenteRepository utenteRepository;
    private final TransazioneContoRepository transazioneContoRepository;
    private final StoricoMovimentoContoRepository storicoMovimentoContoRepository;
    private final TipologiaTransazioneRepository tipologiaTransazioneRepository;
    private final TipologiaMovimentoRepository tipologiaMovimentoRepository;
    private final TransazioneContoMapper transazioneContoMapper;
    private final StoricoMovimentoContoMapper storicoMovimentoContoMapper;



    @Override
    @Transactional(readOnly = true)
    public List<ContoDTO> getAllConti() {
        return contoRepository.findAll().stream()
                .map(contoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ContoDTO getContoById(Integer id) {
        return contoRepository.findById(id)
                .map(contoMapper::toDTO)
                .orElseThrow(() -> new ContoNotFoundException("Conto non trovato con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public ContoDTO getContoByIban(String iban) {
        return contoRepository.findByIban(iban)
                .map(contoMapper::toDTO)
                .orElseThrow(() -> new ContoNotFoundException("Conto non trovato con IBAN: " + iban));
    }

    @Override
    public ContoDTO createConto(ContoDTO dto) {
        // Verifica che l'utente esista
        Utente utente = utenteRepository.findById(dto.getUtenteId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + dto.getUtenteId()));

        // Verifica che l'IBAN non sia già in uso
        if (contoRepository.existsByIban(dto.getIban())) {
            throw new RuntimeException("IBAN già esistente: " + dto.getIban());
        }

        Conto conto = Conto.builder()
                .iban(dto.getIban())
                .utente(utente)
                .tipoConto(dto.getTipoConto())
                .saldoDisponibile(dto.getSaldoDisponibile() != null ? dto.getSaldoDisponibile() : BigDecimal.ZERO)
                .saldoContabile(dto.getSaldoContabile() != null ? dto.getSaldoContabile() : BigDecimal.ZERO)
                .dataApertura(LocalDateTime.now())
                .stato("ATTIVO")
                .contoAperto(true)
                .build();

        conto = contoRepository.save(conto);
        log.info("Creato nuovo conto con ID: {} per utente: {}", conto.getContoId(), utente.getCodiceFiscale());

        return contoMapper.toDTO(conto);
    }

    @Override
    public ContoDTO updateConto(Integer id, ContoDTO dto) {
        Conto conto = contoRepository.findById(id)
                .orElseThrow(() -> new ContoNotFoundException("Conto non trovato con ID: " + id));

        conto.setTipoConto(dto.getTipoConto());
        conto.setSaldoDisponibile(dto.getSaldoDisponibile());
        conto.setSaldoContabile(dto.getSaldoContabile());

        return contoMapper.toDTO(contoRepository.save(conto));
    }

    @Override
    public void deleteConto(Integer id) {
        Conto conto = contoRepository.findById(id)
                .orElseThrow(() -> new ContoNotFoundException("Conto non trovato con ID: " + id));

        if (conto.getSaldoDisponibile().compareTo(BigDecimal.ZERO) != 0) {
            throw new RuntimeException("Non è possibile eliminare un conto con saldo diverso da zero");
        }

        contoRepository.deleteById(id);
        log.info("Eliminato conto con ID: {}", id);
    }

    @Override
    public ContoDTO chiudiConto(Integer id) {
        Conto conto = contoRepository.findById(id)
                .orElseThrow(() -> new ContoNotFoundException("Conto non trovato con ID: " + id));

        conto.setContoAperto(false);
        conto.setStato("CHIUSO");

        conto = contoRepository.save(conto);
        log.info("Chiuso conto con ID: {}", id);

        return contoMapper.toDTO(conto);
    }

    @Override
    public ContoDTO riapriConto(Integer id) {
        Conto conto = contoRepository.findById(id)
                .orElseThrow(() -> new ContoNotFoundException("Conto non trovato con ID: " + id));

        conto.setContoAperto(true);
        conto.setStato("ATTIVO");

        conto = contoRepository.save(conto);
        log.info("Riaperto conto con ID: {}", id);

        return contoMapper.toDTO(conto);
    }


    @Override
    public ContoDTO deposito(Integer id, BigDecimal importo, String descrizione) {
        Conto conto = contoRepository.findById(id)
                .orElseThrow(() -> new ContoNotFoundException("Conto non trovato con ID: " + id));

        if (!conto.getContoAperto()) {
            throw new ContoChiusoException("Non è possibile effettuare operazioni su un conto chiuso");
        }

        if (importo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("L'importo deve essere positivo");
        }

        // Aggiorna i saldi
        conto.setSaldoDisponibile(conto.getSaldoDisponibile().add(importo));
        conto.setSaldoContabile(conto.getSaldoContabile().add(importo));

        conto = contoRepository.save(conto);

        // Registra la transazione
        registraTransazione(conto, importo, "DEPOSITO", descrizione != null ? descrizione : "Deposito");

        // Registra il movimento
        registraMovimento(conto, importo, "DEPOSITO", DirezioneMovimento.ENTRATA,
                descrizione != null ? descrizione : "Deposito", conto.getSaldoDisponibile());

        log.info("Effettuato deposito di {} sul conto ID: {}", importo, id);

        return contoMapper.toDTO(conto);
    }

    @Override
    public ContoDTO prelievo(Integer id, BigDecimal importo, String descrizione) {
        Conto conto = contoRepository.findById(id)
                .orElseThrow(() -> new ContoNotFoundException("Conto non trovato con ID: " + id));

        if (!conto.getContoAperto()) {
            throw new ContoChiusoException("Non è possibile effettuare operazioni su un conto chiuso");
        }

        if (importo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("L'importo deve essere positivo");
        }

        if (conto.getSaldoDisponibile().compareTo(importo) < 0) {
            throw new SaldoInsufficienteException("Saldo insufficiente per effettuare il prelievo");
        }

        // Aggiorna i saldi
        conto.setSaldoDisponibile(conto.getSaldoDisponibile().subtract(importo));
        conto.setSaldoContabile(conto.getSaldoContabile().subtract(importo));

        conto = contoRepository.save(conto);

        // Registra la transazione
        registraTransazione(conto, importo, "PRELIEVO", descrizione != null ? descrizione : "Prelievo");

        // Registra il movimento
        registraMovimento(conto, importo, "PRELIEVO", DirezioneMovimento.USCITA,
                descrizione != null ? descrizione : "Prelievo", conto.getSaldoDisponibile());

        log.info("Effettuato prelievo di {} dal conto ID: {}", importo, id);

        return contoMapper.toDTO(conto);
    }

    @Override
    public void bonifico(Integer contoMittenteId, BonificoDTO bonifico) {
        Conto contoMittente = contoRepository.findById(contoMittenteId)
                .orElseThrow(() -> new ContoNotFoundException("Conto mittente non trovato con ID: " + contoMittenteId));

        Conto contoBeneficiario = contoRepository.findByIban(bonifico.getIbanBeneficiario())
                .orElseThrow(() -> new ContoNotFoundException("Conto beneficiario non trovato con IBAN: " + bonifico.getIbanBeneficiario()));

        if (!contoMittente.getContoAperto()) {
            throw new ContoChiusoException("Il conto mittente è chiuso");
        }

        if (!contoBeneficiario.getContoAperto()) {
            throw new ContoChiusoException("Il conto beneficiario è chiuso");
        }

        if (contoMittente.getSaldoDisponibile().compareTo(bonifico.getImporto()) < 0) {
            throw new SaldoInsufficienteException("Saldo insufficiente per effettuare il bonifico");
        }

        // Aggiorna saldo mittente (sottrazione)
        contoMittente.setSaldoDisponibile(contoMittente.getSaldoDisponibile().subtract(bonifico.getImporto()));
        contoMittente.setSaldoContabile(contoMittente.getSaldoContabile().subtract(bonifico.getImporto()));

        // Aggiorna saldo beneficiario (addizione)
        contoBeneficiario.setSaldoDisponibile(contoBeneficiario.getSaldoDisponibile().add(bonifico.getImporto()));
        contoBeneficiario.setSaldoContabile(contoBeneficiario.getSaldoContabile().add(bonifico.getImporto()));

        contoRepository.save(contoMittente);
        contoRepository.save(contoBeneficiario);

        // Registra transazioni
        String descrizioneOut = "Bonifico a " + bonifico.getNomeBeneficiario() + " - " + bonifico.getCausale();
        String descrizioneIn = "Bonifico da " + contoMittente.getUtente().getNome() + " " + contoMittente.getUtente().getCognome() + " - " + bonifico.getCausale();

        registraTransazione(contoMittente, bonifico.getImporto(), "BONIFICO_OUT", descrizioneOut);
        registraTransazione(contoBeneficiario, bonifico.getImporto(), "BONIFICO_IN", descrizioneIn);

        // Registra movimenti
        registraMovimento(contoMittente, bonifico.getImporto(), "BONIFICO_OUT", DirezioneMovimento.USCITA,
                descrizioneOut, contoMittente.getSaldoDisponibile());
        registraMovimento(contoBeneficiario, bonifico.getImporto(), "BONIFICO_IN", DirezioneMovimento.ENTRATA,
                descrizioneIn, contoBeneficiario.getSaldoDisponibile());

        log.info("Effettuato bonifico di {} da conto {} a conto {}",
                bonifico.getImporto(), contoMittente.getIban(), contoBeneficiario.getIban());
    }


    @Override
    @Transactional(readOnly = true)
    public List<ContoDTO> getContiByCodiceFiscale(String codiceFiscale) {
        return contoRepository.findAll().stream()
                .filter(conto -> conto.getUtente().getCodiceFiscale().equals(codiceFiscale))
                .map(contoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContoDTO> getContiByNomeECognome(String nome, String cognome) {
        return contoRepository.findByUtente_NomeAndUtente_Cognome(nome, cognome).stream()
                .map(contoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransazioneContoDTO> getTransazioniConto(Integer contoId, Pageable pageable) {
        if (!contoRepository.existsById(contoId)) {
            throw new ContoNotFoundException("Conto non trovato con ID: " + contoId);
        }

        return transazioneContoRepository.findByConto_ContoId(contoId, pageable)
                .map(transazioneContoMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StoricoMovimentoContoDTO> getMovimentiConto(Integer contoId, Pageable pageable) {
        if (!contoRepository.existsById(contoId)) {
            throw new ContoNotFoundException("Conto non trovato con ID: " + contoId);
        }

        return storicoMovimentoContoRepository.findByConto_ContoId(contoId, pageable)
                .map(storicoMovimentoContoMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean ibanExists(String iban) {
        return contoRepository.existsByIban(iban);
    }

    private void registraTransazione(Conto conto, BigDecimal importo, String tipoTransazione, String descrizione) {
        // Trova o crea la tipologia di transazione
        TipologiaTransazione tipologia = tipologiaTransazioneRepository.findByCodice(tipoTransazione)
                .orElseGet(() -> {
                    TipologiaTransazione nuovaTipologia = TipologiaTransazione.builder()
                            .codice(tipoTransazione)
                            .descrizione(tipoTransazione.replace("_", " "))
                            .dataCreazione(LocalDateTime.now())
                            .build();
                    return tipologiaTransazioneRepository.save(nuovaTipologia);
                });

        TransazioneConto transazione = TransazioneConto.builder()
                .conto(conto)
                .tipologiaTransazione(tipologia)
                .importo(importo)
                .descrizione(descrizione)
                .dataOperazione(LocalDateTime.now())
                .statoTransazione("COMPLETATA")
                .build();

        transazioneContoRepository.save(transazione);
    }

    private void registraMovimento(Conto conto, BigDecimal importo, String tipoMovimento,
                                   DirezioneMovimento direzione, String descrizione, BigDecimal saldoDopoMovimento) {
        // Trova o crea la tipologia di movimento
        TipologiaMovimento tipologia = tipologiaMovimentoRepository.findByCodice(tipoMovimento)
                .orElseGet(() -> {
                    TipologiaMovimento nuovaTipologia = TipologiaMovimento.builder()
                            .codice(tipoMovimento)
                            .descrizione(tipoMovimento.replace("_", " "))
                            .direzione(direzione)
                            .dataCreazione(LocalDateTime.now())
                            .build();
                    return tipologiaMovimentoRepository.save(nuovaTipologia);
                });

        StoricoMovimentoConto movimento = StoricoMovimentoConto.builder()
                .conto(conto)
                .tipologiaMovimento(tipologia)
                .importo(importo)
                .descrizione(descrizione)
                .saldoDopoMovimento(saldoDopoMovimento)
                .dataMovimento(LocalDateTime.now())
                .build();

        storicoMovimentoContoRepository.save(movimento);
    }
}
