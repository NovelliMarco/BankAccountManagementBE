package com.project.bank_account_management_be.service.impl;

import com.project.bank_account_management_be.dto.CartaDTO;
import com.project.bank_account_management_be.dto.TransazioneCartaDTO;
import com.project.bank_account_management_be.entity.Carta;
import com.project.bank_account_management_be.mapper.CartaMapper;
import com.project.bank_account_management_be.mapper.TransazioneCartaMapper;
import com.project.bank_account_management_be.repository.CartaRepository;
import com.project.bank_account_management_be.repository.TransazioneCartaRepository;
import com.project.bank_account_management_be.service.CartaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartaServiceImpl implements CartaService {

    private final CartaRepository cartaRepository;
    private final CartaMapper cartaMapper;
    private final TransazioneCartaRepository transazioneCartaRepository;
    private final TransazioneCartaMapper transazioneCartaMapper;


    @Override
    public List<CartaDTO> getAllCarte() {
        return cartaRepository.findAll().stream()
                .map(cartaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CartaDTO getCartaById(Integer id) {
        return cartaRepository.findById(id)
                .map(cartaMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Carta non trovata"));
    }

    @Override
    public CartaDTO createCarta(CartaDTO dto) {
        Carta carta = Carta.builder()
                .numeroCarta(dto.getNumeroCarta())
                .tipoCarta(dto.getTipoCarta())
                .dataScadenza(dto.getDataScadenza())
                .limiteGiornaliero(dto.getLimiteGiornaliero())
                .limiteMensile(dto.getLimiteMensile())
                .limiteAnnuale(dto.getLimiteAnnuale())
                .stato("ATTIVA")
                .bloccata(false)
                .dataCreazione(LocalDateTime.now())
                .build();
        return cartaMapper.toDTO(cartaRepository.save(carta));
    }

    @Override
    public CartaDTO updateCarta(Integer id, CartaDTO dto) {
        Carta carta = cartaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carta non trovata"));
        carta.setTipoCarta(dto.getTipoCarta());
        carta.setDataScadenza(dto.getDataScadenza());
        carta.setLimiteGiornaliero(dto.getLimiteGiornaliero());
        carta.setLimiteMensile(dto.getLimiteMensile());
        carta.setLimiteAnnuale(dto.getLimiteAnnuale());
        return cartaMapper.toDTO(cartaRepository.save(carta));
    }

    @Override
    public void deleteCarta(Integer id) {
        cartaRepository.deleteById(id);
    }

    @Override
    public CartaDTO bloccaCarta(Integer id) {
        Carta carta = cartaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carta non trovata"));
        carta.setBloccata(true);
        carta.setStato("BLOCCATA");
        carta.setDataBlocco(LocalDateTime.now());
        return cartaMapper.toDTO(cartaRepository.save(carta));
    }

    @Override
    public CartaDTO sbloccaCarta(Integer id) {
        Carta carta = cartaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carta non trovata"));
        carta.setBloccata(false);
        carta.setStato("ATTIVA");
        carta.setDataBlocco(null);
        return cartaMapper.toDTO(cartaRepository.save(carta));
    }

    @Override
    public List<CartaDTO> getCarteByCodiceFiscale(String codiceFiscale) {
        return cartaRepository.findByUtente_CodiceFiscale(codiceFiscale)
                .stream()
                .map(cartaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CartaDTO> getCarteByNomeECognome(String nome, String cognome) {
        return cartaRepository.findByUtente_NomeAndUtente_Cognome(nome, cognome)
                .stream()
                .map(cartaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long countCarteByCodiceFiscale(String codiceFiscale) {
        return cartaRepository.countByUtente_CodiceFiscale(codiceFiscale);
    }

    @Override
    public Page<TransazioneCartaDTO> getTransazioniCarta(Integer cartaId, Pageable pageable) {
        return transazioneCartaRepository.findByCarta_CartaId(cartaId, pageable)
                .map(transazioneCartaMapper::toDTO);
    }

}
