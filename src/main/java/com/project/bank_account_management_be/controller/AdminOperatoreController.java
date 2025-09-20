package com.project.bank_account_management_be.controller;

import com.project.bank_account_management_be.dto.ContoDTO;
import com.project.bank_account_management_be.dto.CreateContoDTO;
import com.project.bank_account_management_be.dto.CreateUtenteDTO;
import com.project.bank_account_management_be.dto.UtenteDTO;
import com.project.bank_account_management_be.service.ContoService;
import com.project.bank_account_management_be.service.UtenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Amministrazione Banca", description = "Operazioni riservate ad Amministratori e Operatori")
@SecurityRequirement(name = "bankAuth")
public class AdminOperatoreController {

    private final UtenteService utenteService;
    private final ContoService contoService;

    // ==================== GESTIONE UTENTI ====================

    @Operation(summary = "Crea nuovo utente",
            description = "Crea un nuovo utente nel sistema. Solo AMMINISTRATORE e OPERATORE possono eseguire questa operazione")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Utente creato con successo"),
            @ApiResponse(responseCode = "400", description = "Dati non validi"),
            @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo insufficiente"),
            @ApiResponse(responseCode = "409", description = "Utente già esistente")
    })
    @PostMapping("/utenti")
    @PreAuthorize("hasRole('AMMINISTRATORE') or hasRole('OPERATORE')")
    public ResponseEntity<UtenteDTO> createUtente(
            @Valid @RequestBody CreateUtenteDTO dto,
            @Parameter(description = "ID dell'operatore che effettua l'operazione", required = true)
            @RequestHeader("X-Operator-Id") Integer operatorId) {

        UtenteDTO createdUtente = utenteService.createUtenteByOperator(dto, operatorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUtente);
    }

    @Operation(summary = "Lista tutti gli utenti",
            description = "Recupera la lista di tutti gli utenti del sistema")
    @ApiResponse(responseCode = "200", description = "Lista utenti recuperata con successo")
    @GetMapping("/utenti")
    @PreAuthorize("hasRole('AMMINISTRATORE') or hasRole('OPERATORE')")
    public ResponseEntity<List<UtenteDTO>> getAllUtentiForAdmin() {
        return ResponseEntity.ok(utenteService.getAllUtentiForAdmin());
    }

    @Operation(summary = "Cerca utenti per codice fiscale",
            description = "Cerca un utente specifico per codice fiscale")
    @GetMapping("/utenti/cerca/{codiceFiscale}")
    @PreAuthorize("hasRole('AMMINISTRATORE') or hasRole('OPERATORE')")
    public ResponseEntity<UtenteDTO> searchUtenteByCodiceFiscale(
            @Parameter(description = "Codice fiscale da cercare", required = true)
            @PathVariable String codiceFiscale) {
        return ResponseEntity.ok(utenteService.getUtenteByCodiceFiscale(codiceFiscale));
    }

    @Operation(summary = "Aggiorna utente esistente",
            description = "Aggiorna i dati di un utente esistente")
    @PutMapping("/utenti/{id}")
    @PreAuthorize("hasRole('AMMINISTRATORE') or hasRole('OPERATORE')")
    public ResponseEntity<UtenteDTO> updateUtenteByOperator(
            @PathVariable Integer id,
            @Valid @RequestBody UtenteDTO dto,
            @RequestHeader("X-Operator-Id") Integer operatorId) {
        return ResponseEntity.ok(utenteService.updateUtenteByOperator(id, dto, operatorId));
    }

    // ==================== GESTIONE CONTI ====================

    @Operation(summary = "Crea nuovo conto per utente",
            description = "Crea un nuovo conto bancario per un utente esistente. Solo AMMINISTRATORE e OPERATORE possono eseguire questa operazione")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conto creato con successo"),
            @ApiResponse(responseCode = "400", description = "Dati non validi"),
            @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo insufficiente"),
            @ApiResponse(responseCode = "404", description = "Utente non trovato"),
            @ApiResponse(responseCode = "409", description = "IBAN già esistente")
    })
    @PostMapping("/conti")
    @PreAuthorize("hasRole('AMMINISTRATORE') or hasRole('OPERATORE')")
    public ResponseEntity<ContoDTO> createContoForUtente(
            @Valid @RequestBody CreateContoDTO dto,
            @Parameter(description = "ID dell'operatore che effettua l'operazione", required = true)
            @RequestHeader("X-Operator-Id") Integer operatorId) {

        ContoDTO createdConto = contoService.createContoByOperator(dto, operatorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConto);
    }

    @Operation(summary = "Lista conti per utente",
            description = "Recupera tutti i conti di un utente specifico")
    @GetMapping("/conti/utente/{codiceFiscale}")
    @PreAuthorize("hasRole('AMMINISTRATORE') or hasRole('OPERATORE')")
    public ResponseEntity<List<ContoDTO>> getContiByUtenteForAdmin(
            @Parameter(description = "Codice fiscale dell'utente", required = true)
            @PathVariable String codiceFiscale) {
        return ResponseEntity.ok(contoService.getContiByCodiceFiscale(codiceFiscale));
    }

    @Operation(summary = "Chiudi conto utente",
            description = "Chiude un conto bancario di un utente")
    @PutMapping("/conti/{contoId}/chiudi")
    @PreAuthorize("hasRole('AMMINISTRATORE') or hasRole('OPERATORE')")
    public ResponseEntity<ContoDTO> chiudiContoByOperator(
            @PathVariable Integer contoId,
            @RequestHeader("X-Operator-Id") Integer operatorId,
            @Parameter(description = "Motivo della chiusura") @RequestParam(required = false) String motivo) {
        return ResponseEntity.ok(contoService.chiudiContoByOperator(contoId, operatorId, motivo));
    }

    @Operation(summary = "Riapri conto utente",
            description = "Riapre un conto bancario precedentemente chiuso")
    @PutMapping("/conti/{contoId}/riapri")
    @PreAuthorize("hasRole('AMMINISTRATORE') or hasRole('OPERATORE')")
    public ResponseEntity<ContoDTO> riapriContoByOperator(
            @PathVariable Integer contoId,
            @RequestHeader("X-Operator-Id") Integer operatorId,
            @Parameter(description = "Motivo della riapertura") @RequestParam(required = false) String motivo) {
        return ResponseEntity.ok(contoService.riapriContoByOperator(contoId, operatorId, motivo));
    }

    @Operation(summary = "Forza deposito su conto",
            description = "Effettua un deposito forzato su un conto (operazioni amministrative)")
    @PostMapping("/conti/{contoId}/deposito-forzato")
    @PreAuthorize("hasRole('AMMINISTRATORE')")
    public ResponseEntity<ContoDTO> depositoForzato(
            @PathVariable Integer contoId,
            @RequestParam java.math.BigDecimal importo,
            @RequestParam String motivo,
            @RequestHeader("X-Operator-Id") Integer operatorId) {
        return ResponseEntity.ok(contoService.depositoForzatoByAdmin(contoId, importo, motivo, operatorId));
    }

    @Operation(summary = "Recupera tutti i conti",
            description = "Recupera tutti i conti del sistema per amministrazione")
    @GetMapping("/conti")
    @PreAuthorize("hasRole('AMMINISTRATORE') or hasRole('OPERATORE')")
    public ResponseEntity<List<ContoDTO>> getAllContiForAdmin() {
        return ResponseEntity.ok(contoService.getAllConti());
    }

    // ==================== OPERAZIONI DI REPORTISTICA ====================

    @Operation(summary = "Statistiche utenti",
            description = "Recupera statistiche generali sugli utenti")
    @GetMapping("/statistiche/utenti")
    @PreAuthorize("hasRole('AMMINISTRATORE')")
    public ResponseEntity<Object> getStatisticheUtenti() {
        return ResponseEntity.ok(utenteService.getStatisticheUtenti());
    }

    @Operation(summary = "Statistiche conti",
            description = "Recupera statistiche generali sui conti")
    @GetMapping("/statistiche/conti")
    @PreAuthorize("hasRole('AMMINISTRATORE')")
    public ResponseEntity<Object> getStatisticheConti() {
        return ResponseEntity.ok(contoService.getStatisticheConti());
    }
}