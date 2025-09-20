package com.project.bank_account_management_be.repository;

import com.project.bank_account_management_be.entity.Ruolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RuoloRepository extends JpaRepository<Ruolo, Integer> {
    Optional<Ruolo> findByNomeRuolo(String cliente);
}