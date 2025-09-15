package com.project.bank_account_management_be.repository;

import com.project.bank_account_management_be.entity.Conto;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface ContoRepository extends Repository<Conto, Integer> {
    Optional<Conto> findByIban(String iban);

    List<Conto> findByUtente_NomeAndUtente_Cognome(String nome, String cognome);

    boolean existsByIban(String iban);

    long deleteByIban(String iban);
}