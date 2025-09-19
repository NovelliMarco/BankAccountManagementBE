package com.project.bank_account_management_be.repository;

import com.project.bank_account_management_be.entity.TransazioneCarta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransazioneCartaRepository extends JpaRepository<TransazioneCarta, Integer> {

    Page<TransazioneCarta> findByCarta_CartaId(Integer cartaId, Pageable pageable);

}