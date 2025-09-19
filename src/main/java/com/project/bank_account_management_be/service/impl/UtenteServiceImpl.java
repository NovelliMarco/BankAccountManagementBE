package com.project.bank_account_management_be.service.impl;

import com.project.bank_account_management_be.dto.UtenteDTO;
import com.project.bank_account_management_be.entity.Login;
import com.project.bank_account_management_be.entity.Utente;
import com.project.bank_account_management_be.error.UtenteNotFoundException;
import com.project.bank_account_management_be.mapper.UtenteMapper;
import com.project.bank_account_management_be.repository.UtenteRepository;
import com.project.bank_account_management_be.service.UtenteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UtenteServiceImpl implements UtenteService {

    private final UtenteRepository utenteRepository;
    private final UtenteMapper utenteMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<UtenteDTO> getAllUtenti(Pageable pageable) {
        return utenteRepository.findAll(pageable)
                .map(utenteMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public UtenteDTO getUtenteById(Integer id) {
        return utenteRepository.findById(id)
                .map(utenteMapper::toDTO)
                .orElseThrow(() -> new UtenteNotFoundException("Utente non trovato con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public UtenteDTO getUtenteByCodiceFiscale(String codiceFiscale) {
        return utenteRepository.findByCodiceFiscale(codiceFiscale)
                .map(utenteMapper::toDTO)
                .orElseThrow(() -> new UtenteNotFoundException("Utente non trovato con codice fiscale: " + codiceFiscale));
    }

    @Override
    public UtenteDTO createUtente(UtenteDTO dto) {
        // Verifica che email e codice fiscale non siano già in uso
        if (utenteRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email già esistente: " + dto.getEmail());
        }

        if (utenteRepository.existsByCodiceFiscale(dto.getCodiceFiscale())) {
            throw new RuntimeException("Codice fiscale già esistente: " + dto.getCodiceFiscale());
        }

        // Crea login di base (password dovrà essere impostata successivamente)
        Login login = Login.builder()
                .email(dto.getEmail())
                .passwordHash("") // Da implementare con hashing appropriato
                .build();

        Utente utente = Utente.builder()
                .nome(dto.getNome())
                .cognome(dto.getCognome())
                .dataNascita(dto.getDataNascita())
                .professione(dto.getProfessione())
                .codiceFiscale(dto.getCodiceFiscale())
                .indirizzo(dto.getIndirizzo())
                .citta(dto.getCitta())
                .cap(dto.getCap())
                .provincia(dto.getProvincia())
                .nazione(dto.getNazione())
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .login(login)
                .dataCreazione(LocalDateTime.now())
                .build();

        utente = utenteRepository.save(utente);
        log.info("Creato nuovo utente con ID: {} - {}", utente.getUtenteId(), utente.getCodiceFiscale());

        return utenteMapper.toDTO(utente);
    }

    @Override
    public UtenteDTO updateUtente(Integer id, UtenteDTO dto) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new UtenteNotFoundException("Utente non trovato con ID: " + id));

        // Verifica che email non sia già in uso da altri utenti
        if (!utente.getEmail().equals(dto.getEmail()) && utenteRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email già esistente: " + dto.getEmail());
        }

        utente.setNome(dto.getNome());
        utente.setCognome(dto.getCognome());
        utente.setDataNascita(dto.getDataNascita());
        utente.setProfessione(dto.getProfessione());
        utente.setIndirizzo(dto.getIndirizzo());
        utente.setCitta(dto.getCitta());
        utente.setCap(dto.getCap());
        utente.setProvincia(dto.getProvincia());
        utente.setNazione(dto.getNazione());
        utente.setTelefono(dto.getTelefono());
        utente.setEmail(dto.getEmail());

        // Aggiorna anche l'email nel login se presente
        if (utente.getLogin() != null) {
            utente.getLogin().setEmail(dto.getEmail());
        }

        utente = utenteRepository.save(utente);
        log.info("Aggiornato utente con ID: {} - {}", id, utente.getCodiceFiscale());

        return utenteMapper.toDTO(utente);
    }

    @Override
    public void deleteUtente(Integer id) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new UtenteNotFoundException("Utente non trovato con ID: " + id));

        // Verifica che l'utente non abbia conti o carte attivi
        if (!utente.getConti().isEmpty()) {
            throw new RuntimeException("Non è possibile eliminare un utente con conti associati");
        }

        if (!utente.getCarte().isEmpty()) {
            throw new RuntimeException("Non è possibile eliminare un utente con carte associate");
        }

        utenteRepository.deleteById(id);
        log.info("Eliminato utente con ID: {} - {}", id, utente.getCodiceFiscale());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UtenteDTO> searchUtenti(String nome, String cognome) {
        List<Utente> utenti;

        if (nome != null && cognome != null) {
            utenti = utenteRepository.findByNomeContainingIgnoreCaseAndCognomeContainingIgnoreCase(nome, cognome);
        } else if (nome != null) {
            utenti = utenteRepository.findByNomeContainingIgnoreCase(nome);
        } else if (cognome != null) {
            utenti = utenteRepository.findByCognomeContainingIgnoreCase(cognome);
        } else {
            utenti = utenteRepository.findAll();
        }

        return utenti.stream()
                .map(utenteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return utenteRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean codiceFiscaleExists(String codiceFiscale) {
        return utenteRepository.existsByCodiceFiscale(codiceFiscale);
    }
}