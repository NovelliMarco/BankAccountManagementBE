package com.project.bank_account_management_be.repository;

import com.project.bank_account_management_be.entity.Conto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContoRepository extends JpaRepository<Conto, Integer> {
    Optional<Conto> findByUtente_CodiceFiscale(String codiceFiscale);

    Optional<Conto> findByIban(String iban);

    List<Conto> findByUtente_NomeAndUtente_Cognome(String nome, String cognome);

    boolean existsByIban(String iban);

    long deleteByIban(String iban);
}