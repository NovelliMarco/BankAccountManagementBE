package com.project.bank_account_management_be.service;

import com.project.bank_account_management_be.dto.CreateUtenteDTO;
import com.project.bank_account_management_be.dto.DeleteUtenteDTO;
import com.project.bank_account_management_be.dto.UtenteDTO;
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
}
