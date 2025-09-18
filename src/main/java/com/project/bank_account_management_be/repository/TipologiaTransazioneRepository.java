package com.project.bank_account_management_be.repository;

import com.project.bank_account_management_be.entity.TipologiaTransazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipologiaTransazioneRepository extends JpaRepository<TipologiaTransazione, Integer> {
    Optional<TipologiaTransazione> findByCodice(String codice);
}