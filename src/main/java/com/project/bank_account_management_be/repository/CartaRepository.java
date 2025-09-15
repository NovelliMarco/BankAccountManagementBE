package com.project.bank_account_management_be.repository;

import com.project.bank_account_management_be.entity.Carta;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface CartaRepository extends Repository<Carta, Integer> {
    long countByUtente_CodiceFiscale(String codiceFiscale);

    Optional<Carta> findByNumeroCarta(String numeroCarta);

    List<Carta> findByUtente_NomeAndUtente_Cognome(String nome, String cognome);

    List<Carta> findByUtente_CodiceFiscale(String codiceFiscale);

}