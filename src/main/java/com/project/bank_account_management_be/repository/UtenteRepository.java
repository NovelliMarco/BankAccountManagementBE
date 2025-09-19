package com.project.bank_account_management_be.repository;

import com.project.bank_account_management_be.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bank_account_management_be.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Integer> {

    Optional<Utente> findByCodiceFiscale(String codiceFiscale);

    Optional<Utente> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByCodiceFiscale(String codiceFiscale);

    List<Utente> findByNomeContainingIgnoreCaseAndCognomeContainingIgnoreCase(String nome, String cognome);

    List<Utente> findByNomeContainingIgnoreCase(String nome);

    List<Utente> findByCognomeContainingIgnoreCase(String cognome);

    List<Utente> findByRuolo_NomeRuolo(String nomeRuolo);
}