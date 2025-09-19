package com.project.bank_account_management_be.repository;

import com.project.bank_account_management_be.entity.Carta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartaRepository extends JpaRepository<Carta, Integer> {
    long countByUtente_CodiceFiscale(String codiceFiscale);

    Optional<Carta> findByNumeroCarta(String numeroCarta);

    List<Carta> findByUtente_NomeAndUtente_Cognome(String nome, String cognome);

    List<Carta> findByUtente_CodiceFiscale(String codiceFiscale);

    List<Carta> findAll();

    Optional<Carta> findById(Integer cartaId);

    Carta save(Carta carta);

    void deleteById(Integer id);

    boolean existsByNumeroCarta(String numeroCarta);
}