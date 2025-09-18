package com.project.bank_account_management_be.repository;

import com.project.bank_account_management_be.entity.TipologiaMovimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipologiaMovimentoRepository extends JpaRepository<TipologiaMovimento, Integer> {
    Optional<TipologiaMovimento> findByCodice(String codice);
}