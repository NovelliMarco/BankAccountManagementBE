package com.project.bank_account_management_be.repository;

import com.project.bank_account_management_be.entity.StoricoMovimentoConto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoricoMovimentoContoRepository extends JpaRepository<StoricoMovimentoConto, Integer> {
}