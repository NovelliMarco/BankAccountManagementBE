package com.project.bank_account_management_be.repository;

import com.project.bank_account_management_be.entity.TransazioneConto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransazioneContoRepository extends JpaRepository<TransazioneConto, Integer> {
    Page<TransazioneConto> findByConto_ContoId(Integer contoId, Pageable pageable);
}
