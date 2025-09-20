package com.project.bank_account_management_be.service.impl;

import com.project.bank_account_management_be.dto.CartaDTO;
import com.project.bank_account_management_be.dto.TransazioneCartaDTO;
import com.project.bank_account_management_be.entity.Carta;
import com.project.bank_account_management_be.entity.Utente;
import com.project.bank_account_management_be.mapper.CartaMapper;
import com.project.bank_account_management_be.mapper.TransazioneCartaMapper;
import com.project.bank_account_management_be.repository.CartaRepository;
import com.project.bank_account_management_be.repository.TransazioneCartaRepository;
import com.project.bank_account_management_be.repository.UtenteRepository;
import com.project.bank_account_management_be.service.CartaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CartaServiceImpl implements CartaService {

    private final CartaRepository cartaRepository;
    private final CartaMapper cartaMapper;
    private final TransazioneCartaRepository transazioneCartaRepository;
    private final TransazioneCartaMapper transazioneCartaMapper;
    private final UtenteRepository utenteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CartaDTO> getAllCarte() {
        return cartaRepository.findAll().stream()
                .map(cartaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CartaDTO getCartaById(Integer id) {
        return cartaRepository.findById(id)
                .map(cartaMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Carta non trovata con ID: " + id));
    }

    @Override
    public CartaDTO createCarta(CartaDTO dto) {
        // Verifica che l'utente esista
        Utente utente = utenteRepository.findById(dto.getUtenteId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + dto.getUtenteId()));

        // Genera numero carta se non fornito
        String numeroCarta = dto.getNumeroCarta();
        if (numeroCarta == null || numeroCarta.isEmpty()) {
            numeroCarta = generaNumeroCarta();
        }

        // Verifica che il numero carta non sia già in uso
        if (cartaRepository.existsByNumeroCarta(numeroCarta)) {
            throw new RuntimeException("Numero carta già esistente: " + numeroCarta);
        }

        // Genera CVV se non fornito
        String cvv = dto.getCvv();
        if (cvv == null || cvv.isEmpty()) {
            cvv = generaCVV();
        }

        Carta carta = Carta.builder()
                .numeroCarta(numeroCarta)
                .utente(utente) // Associa l'utente alla carta
                .tipoCarta(dto.getTipoCarta())
                .dataScadenza(dto.getDataScadenza())
                .cvv(cvv)
                .limiteGiornaliero(dto.getLimiteGiornaliero())
                .limiteMensile(dto.getLimiteMensile())
                .limiteAnnuale(dto.getLimiteAnnuale())
                .stato("ATTIVA")
                .bloccata(false)
                .dataCreazione(LocalDateTime.now())
                .build();

        carta = cartaRepository.save(carta);
        log.info("Creata nuova carta ID: {} per utente: {} - {}",
                carta.getCartaId(), utente.getCodiceFiscale(), utente.getNome() + " " + utente.getCognome());

        return cartaMapper.toDTO(carta);
    }

    @Override
    public CartaDTO updateCarta(Integer id, CartaDTO dto) {
        Carta carta = cartaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carta non trovata con ID: " + id));

        // Aggiorna solo i campi modificabili (non l'utente associato)
        carta.setTipoCarta(dto.getTipoCarta());
        carta.setDataScadenza(dto.getDataScadenza());
        carta.setLimiteGiornaliero(dto.getLimiteGiornaliero());
        carta.setLimiteMensile(dto.getLimiteMensile());
        carta.setLimiteAnnuale(dto.getLimiteAnnuale());

        carta = cartaRepository.save(carta);
        log.info("Aggiornata carta ID: {}", id);

        return cartaMapper.toDTO(carta);
    }

    @Override
    public void deleteCarta(Integer id) {
        Carta carta = cartaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carta non trovata con ID: " + id));

        // Verifica che la carta non abbia transazioni pendenti
        // Questo controllo potrebbe essere implementato se necessario

        cartaRepository.deleteById(id);
        log.info("Eliminata carta ID: {} - Numero: ****{}",
                id, carta.getNumeroCarta().substring(carta.getNumeroCarta().length() - 4));
    }

    @Override
    public CartaDTO bloccaCarta(Integer id) {
        Carta carta = cartaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carta non trovata con ID: " + id));

        carta.setBloccata(true);
        carta.setStato("BLOCCATA");
        carta.setDataBlocco(LocalDateTime.now());

        carta = cartaRepository.save(carta);
        log.info("Bloccata carta ID: {} - Utente: {}",
                id, carta.getUtente().getCodiceFiscale());

        return cartaMapper.toDTO(carta);
    }

    @Override
    public CartaDTO sbloccaCarta(Integer id) {
        Carta carta = cartaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carta non trovata con ID: " + id));

        carta.setBloccata(false);
        carta.setStato("ATTIVA");
        carta.setDataBlocco(null);

        carta = cartaRepository.save(carta);
        log.info("Sbloccata carta ID: {} - Utente: {}",
                id, carta.getUtente().getCodiceFiscale());

        return cartaMapper.toDTO(carta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartaDTO> getCarteByCodiceFiscale(String codiceFiscale) {
        return cartaRepository.findByUtente_CodiceFiscale(codiceFiscale)
                .stream()
                .map(cartaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartaDTO> getCarteByNomeECognome(String nome, String cognome) {
        return cartaRepository.findByUtente_NomeAndUtente_Cognome(nome, cognome)
                .stream()
                .map(cartaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countCarteByCodiceFiscale(String codiceFiscale) {
        return cartaRepository.countByUtente_CodiceFiscale(codiceFiscale);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransazioneCartaDTO> getTransazioniCarta(Integer cartaId, Pageable pageable) {
        // Verifica che la carta esista
        if (!cartaRepository.existsById(cartaId)) {
            throw new RuntimeException("Carta non trovata con ID: " + cartaId);
        }

        return transazioneCartaRepository.findByCarta_CartaId(cartaId, pageable)
                .map(transazioneCartaMapper::toDTO);
    }

    /**
     * Genera un numero carta casuale di 16 cifre
     */
    private String generaNumeroCarta() {
        Random random = new Random();
        StringBuilder numeroCartaBuilder = new StringBuilder();

        // Genera 16 cifre casuali
        for (int i = 0; i < 16; i++) {
            numeroCartaBuilder.append(random.nextInt(10));
        }

        String numeroCarta = numeroCartaBuilder.toString();

        // Verifica che non esista già
        while (cartaRepository.existsByNumeroCarta(numeroCarta)) {
            numeroCartaBuilder = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                numeroCartaBuilder.append(random.nextInt(10));
            }
            numeroCarta = numeroCartaBuilder.toString();
        }

        return numeroCarta;
    }

    /**
     * Genera un CVV casuale di 3 cifre
     */
    private String generaCVV() {
        Random random = new Random();
        return String.format("%03d", random.nextInt(1000));
    }
}