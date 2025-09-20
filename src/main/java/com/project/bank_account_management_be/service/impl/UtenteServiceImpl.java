package com.project.bank_account_management_be.service.impl;

import com.project.bank_account_management_be.dto.CreateUtenteDTO;
import com.project.bank_account_management_be.dto.DeleteUtenteDTO;
import com.project.bank_account_management_be.dto.UtenteDTO;
import com.project.bank_account_management_be.entity.Login;
import com.project.bank_account_management_be.entity.Ruolo;
import com.project.bank_account_management_be.entity.Utente;
import com.project.bank_account_management_be.error.UtenteNotFoundException;
import com.project.bank_account_management_be.mapper.UtenteMapper;
import com.project.bank_account_management_be.repository.RuoloRepository;
import com.project.bank_account_management_be.repository.UtenteRepository;
import com.project.bank_account_management_be.service.RoleValidationService;
import com.project.bank_account_management_be.service.UtenteService;
import com.project.bank_account_management_be.util.CryptoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UtenteServiceImpl implements UtenteService {

    private final UtenteRepository utenteRepository;
    private final UtenteMapper utenteMapper;
    private final CryptoUtil cryptoUtil;
    private final RoleValidationService roleValidationService;
    private final AuditService auditService;
    private final RuoloRepository ruoloRepository;

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
    public UtenteDTO createUtente(CreateUtenteDTO dto) {
        // Verifica che email e codice fiscale non siano già in uso
        if (utenteRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email già esistente: " + dto.getEmail());
        }

        if (utenteRepository.existsByCodiceFiscale(dto.getCodiceFiscale())) {
            throw new RuntimeException("Codice fiscale già esistente: " + dto.getCodiceFiscale());
        }

        // Cripta la password con SHA-256
        String hashedPassword = cryptoUtil.hashPassword(dto.getPassword());

        // Crea login con password crittografata
        Login login = Login.builder()
                .email(dto.getEmail())
                .passwordHash(hashedPassword)
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
    public void deleteUtente(Integer id, DeleteUtenteDTO deleteDTO) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new UtenteNotFoundException("Utente non trovato con ID: " + id));

        // Verifica che l'utente abbia un login associato
        if (utente.getLogin() == null || utente.getLogin().getPasswordHash() == null) {
            throw new RuntimeException("Impossibile verificare la password per questo utente");
        }

        // Verifica che la password fornita sia corretta
        String hashedInputPassword = cryptoUtil.hashPassword(deleteDTO.getPassword());
        if (!hashedInputPassword.equals(utente.getLogin().getPasswordHash())) {
            throw new RuntimeException("Password errata. Impossibile eliminare l'utente");
        }

        // Verifica che l'utente non abbia conti o carte attivi
        if (!utente.getConti().isEmpty()) {
            throw new RuntimeException("Non è possibile eliminare un utente con conti associati");
        }

        if (!utente.getCarte().isEmpty()) {
            throw new RuntimeException("Non è possibile eliminare un utente con carte associate");
        }

        // Eliminazione a cascata dell'utente e del login associato
        utenteRepository.deleteById(id);
        log.info("Eliminato utente con ID: {} - {} dopo verifica password", id, utente.getCodiceFiscale());
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

    @Override
    public UtenteDTO createUtenteByOperator(CreateUtenteDTO dto, Integer operatoreId) {
        // Valida i permessi dell'operatore
        roleValidationService.validateUserCreationPermissions(operatoreId);

        try {
            // Valida la creazione dell'utente
            validateUtenteCreation(dto);

            // Verifica che email e codice fiscale non siano già in uso
            if (utenteRepository.existsByEmail(dto.getEmail())) {
                throw new RuntimeException("Email già esistente: " + dto.getEmail());
            }

            if (utenteRepository.existsByCodiceFiscale(dto.getCodiceFiscale())) {
                throw new RuntimeException("Codice fiscale già esistente: " + dto.getCodiceFiscale());
            }

            // Cripta la password con SHA-256
            String hashedPassword = cryptoUtil.hashPassword(dto.getPassword());

            // Crea login con password crittografata
            Login login = Login.builder()
                    .email(dto.getEmail())
                    .passwordHash(hashedPassword)
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

            // Assegna ruolo di default
            assignDefaultRole(utente);

            utente = utenteRepository.save(utente);

            // Log audit
            auditService.logCreazioneUtente(operatoreId, utente.getUtenteId(), utente.getCodiceFiscale());

            log.info("Utente creato dall'operatore {}: {} - {}",
                    operatoreId, utente.getCodiceFiscale(), utente.getEmail());

            return utenteMapper.toDTO(utente);

        } catch (Exception e) {
            auditService.logOperazioneConErrore(operatoreId, "CREAZIONE_UTENTE",
                    "Errore creazione utente " + dto.getCodiceFiscale(),
                    e.getMessage(), "UTENTE", null);
            throw e;
        }
    }

    @Override
    public UtenteDTO updateUtenteByOperator(Integer id, UtenteDTO dto, Integer operatoreId) {
        roleValidationService.validateOperatorePermissions(operatoreId);

        try {
            Utente utente = utenteRepository.findById(id)
                    .orElseThrow(() -> new UtenteNotFoundException("Utente non trovato con ID: " + id));

            // Verifica che l'operatore possa operare su questo utente
            roleValidationService.validateTargetUserOperation(operatoreId, id);

            // Verifica che email non sia già in uso da altri utenti
            if (!utente.getEmail().equals(dto.getEmail()) && utenteRepository.existsByEmail(dto.getEmail())) {
                throw new RuntimeException("Email già esistente: " + dto.getEmail());
            }

            // Salva i valori precedenti per audit
            String valoriPrecedenti = String.format("Nome: %s, Cognome: %s, Email: %s",
                    utente.getNome(), utente.getCognome(), utente.getEmail());

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

            // Log audit
            String nuoviValori = String.format("Nome: %s, Cognome: %s, Email: %s",
                    utente.getNome(), utente.getCognome(), utente.getEmail());
            auditService.logOperazioneUtente(operatoreId, "AGGIORNAMENTO_UTENTE",
                    String.format("Aggiornato utente %s - Prima: [%s] - Dopo: [%s]",
                            utente.getCodiceFiscale(), valoriPrecedenti, nuoviValori),
                    id, "UTENTE", id);

            log.info("Utente {} aggiornato dall'operatore {}", utente.getCodiceFiscale(), operatoreId);

            return utenteMapper.toDTO(utente);

        } catch (Exception e) {
            auditService.logOperazioneConErrore(operatoreId, "AGGIORNAMENTO_UTENTE",
                    "Errore aggiornamento utente ID " + id,
                    e.getMessage(), "UTENTE", id);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UtenteDTO> getAllUtentiForAdmin() {
        return utenteRepository.findAll().stream()
                .map(utenteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Object getStatisticheUtenti() {
        long totaleUtenti = utenteRepository.count();

        Map<String, Long> utentiPerRuolo = utenteRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        u -> roleValidationService.getRuoloUtente(u),
                        Collectors.counting()
                ));

        // Statistiche su registrazioni recenti
        LocalDateTime unMeseFA = LocalDateTime.now().minusMonths(1);
        long nuoveRegistrazioni = utenteRepository.findAll().stream()
                .filter(u -> u.getDataCreazione() != null && u.getDataCreazione().isAfter(unMeseFA))
                .count();

        return Map.of(
                "totaleUtenti", totaleUtenti,
                "utentiPerRuolo", utentiPerRuolo,
                "nuoveRegistrazioniUltimoMese", nuoveRegistrazioni
        );
    }

    @Override
    public void validateUtenteCreation(CreateUtenteDTO dto) {
        // Validazioni business specifiche
        if (dto.getDataNascita() != null) {
            LocalDate oggi = LocalDate.now();
            int eta = oggi.getYear() - dto.getDataNascita().getYear();

            if (eta < 18) {
                throw new IllegalArgumentException("L'utente deve essere maggiorenne");
            }

            if (eta > 120) {
                throw new IllegalArgumentException("Data di nascita non valida");
            }
        }

        // Validazione codice fiscale più specifica se necessario
        if (!isValidCodiceFiscale(dto.getCodiceFiscale())) {
            throw new IllegalArgumentException("Formato codice fiscale non valido");
        }
    }

    @Override
    public void assignDefaultRole(Utente utente) {
        // Assegna il ruolo di default "CLIENTE"
        Optional<Ruolo> ruoloCliente = ruoloRepository.findByNomeRuolo("CLIENTE");

        if (ruoloCliente.isPresent()) {
            utente.setRuolo(ruoloCliente.get());
        } else {
            // Crea il ruolo CLIENTE se non esiste
            Ruolo nuovoRuolo = Ruolo.builder()
                    .nomeRuolo("CLIENTE")
                    .descrizione("Cliente della banca")
                    .build();
            ruoloCliente = Optional.of(ruoloRepository.save(nuovoRuolo));
            utente.setRuolo(ruoloCliente.get());
        }
    }

    private boolean isValidCodiceFiscale(String codiceFiscale) {
        // Validazione base del formato del codice fiscale italiano
        return codiceFiscale != null &&
                codiceFiscale.matches("^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$");
    }
}