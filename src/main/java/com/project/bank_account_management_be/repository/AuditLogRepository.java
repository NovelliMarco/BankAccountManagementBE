package com.project.bank_account_management_be.repository;

import com.project.bank_account_management_be.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findByOperatore_UtenteId(Integer operatoreId, Pageable pageable);

    Page<AuditLog> findByUtenteTarget_UtenteId(Integer utenteTargetId, Pageable pageable);

    Page<AuditLog> findByTipoOperazione(String tipoOperazione, Pageable pageable);

    Page<AuditLog> findByDataOperazioneBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT a FROM AuditLog a WHERE a.operatore.utenteId = :operatoreId AND a.dataOperazione >= :dataInizio")
    List<AuditLog> findRecentOperationsByOperatore(@Param("operatoreId") Integer operatoreId,
                                                   @Param("dataInizio") LocalDateTime dataInizio);

    @Query("SELECT COUNT(a) FROM AuditLog a WHERE a.esitoOperazione = false AND a.dataOperazione >= :dataInizio")
    long countErroriRecenti(@Param("dataInizio") LocalDateTime dataInizio);
}