package com.project.bank_account_management_be.repository;

import com.project.bank_account_management_be.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Login, Integer> {
    Optional<Login> findByEmail(String email);

    boolean existsByEmail(String email);
}