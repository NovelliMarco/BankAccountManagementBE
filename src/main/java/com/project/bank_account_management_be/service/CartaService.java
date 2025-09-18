package com.project.bank_account_management_be.service;

import com.project.bank_account_management_be.dto.CartaDTO;
import com.project.bank_account_management_be.dto.TransazioneCartaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartaService {
    List<CartaDTO> getAllCarte();

    CartaDTO getCartaById(Integer id);

    CartaDTO createCarta(CartaDTO dto);

    CartaDTO updateCarta(Integer id, CartaDTO dto);

    void deleteCarta(Integer id);

    CartaDTO bloccaCarta(Integer id);

    CartaDTO sbloccaCarta(Integer id);

    List<CartaDTO> getCarteByCodiceFiscale(String codiceFiscale);

    List<CartaDTO> getCarteByNomeECognome(String nome, String cognome);

    long countCarteByCodiceFiscale(String codiceFiscale);

    Page<TransazioneCartaDTO> getTransazioniCarta(Integer cartaId, Pageable pageable);

}
