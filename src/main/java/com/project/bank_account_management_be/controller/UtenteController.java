package com.project.bank_account_management_be.controller;

import com.project.bank_account_management_be.dto.UtenteDTO;
import com.project.bank_account_management_be.service.UtenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/utenti")
@RequiredArgsConstructor
@Tag(name = "Utenti", description = "Operazioni per la gestione degli utenti")
public class UtenteController {

    private final UtenteService utenteService;

    @Operation(summary = "Recupera tutti gli utenti", description = "Restituisce la lista paginata di tutti gli utenti")
    @ApiResponse(responseCode = "200", description = "Lista degli utenti recuperata con successo")
    @GetMapping
    public ResponseEntity<Page<UtenteDTO>> getAllUtenti(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(utenteService.getAllUtenti(pageable));
    }

    @Operation(summary = "Recupera utente per ID", description = "Restituisce i dettagli di un utente specifico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utente trovato"),
            @ApiResponse(responseCode = "404", description = "Utente non trovato")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UtenteDTO> getUtenteById(
            @Parameter(description = "ID dell'utente", required = true) @PathVariable Integer id) {
        return ResponseEntity.ok(utenteService.getUtenteById(id));
    }

    @Operation(summary = "Recupera utente per codice fiscale", description = "Restituisce i dettagli di un utente tramite codice fiscale")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utente trovato"),
            @ApiResponse(responseCode = "404", description = "Utente non trovato")
    })
    @GetMapping("/codice-fiscale/{codiceFiscale}")
    public ResponseEntity<UtenteDTO> getUtenteByCodiceFiscale(
            @Parameter(description = "Codice fiscale dell'utente", required = true) @PathVariable String codiceFiscale) {
        return ResponseEntity.ok(utenteService.getUtenteByCodiceFiscale(codiceFiscale));
    }

    @Operation(summary = "Crea nuovo utente", description = "Crea un nuovo utente nel sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Utente creato con successo"),
            @ApiResponse(responseCode = "400", description = "Dati non validi"),
            @ApiResponse(responseCode = "409", description = "Utente già esistente")
    })
    @PostMapping
    public ResponseEntity<UtenteDTO> createUtente(@Valid @RequestBody UtenteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(utenteService.createUtente(dto));
    }

    @Operation(summary = "Aggiorna utente", description = "Aggiorna i dati di un utente esistente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utente aggiornato con successo"),
            @ApiResponse(responseCode = "404", description = "Utente non trovato")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UtenteDTO> updateUtente(
            @Parameter(description = "ID dell'utente", required = true) @PathVariable Integer id,
            @Valid @RequestBody UtenteDTO dto) {
        return ResponseEntity.ok(utenteService.updateUtente(id, dto));
    }

    @Operation(summary = "Elimina utente", description = "Elimina un utente dal sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Utente eliminato con successo"),
            @ApiResponse(responseCode = "404", description = "Utente non trovato"),
            @ApiResponse(responseCode = "400", description = "Non è possibile eliminare l'utente (ha conti o carte attivi)")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtente(
            @Parameter(description = "ID dell'utente", required = true) @PathVariable Integer id) {
        utenteService.deleteUtente(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cerca utenti per nome e cognome", description = "Cerca utenti per nome e/o cognome")
    @GetMapping("/search")
    public ResponseEntity<List<UtenteDTO>> searchUtenti(
            @Parameter(description = "Nome dell'utente") @RequestParam(required = false) String nome,
            @Parameter(description = "Cognome dell'utente") @RequestParam(required = false) String cognome) {
        return ResponseEntity.ok(utenteService.searchUtenti(nome, cognome));
    }

    @Operation(summary = "Verifica esistenza email", description = "Verifica se un'email è già utilizzata")
    @GetMapping("/email/{email}/exists")
    public ResponseEntity<Boolean> checkEmailExists(
            @Parameter(description = "Email da verificare", required = true) @PathVariable String email) {
        return ResponseEntity.ok(utenteService.emailExists(email));
    }

    @Operation(summary = "Verifica esistenza codice fiscale", description = "Verifica se un codice fiscale è già utilizzato")
    @GetMapping("/codice-fiscale/{codiceFiscale}/exists")
    public ResponseEntity<Boolean> checkCodiceFiscaleExists(
            @Parameter(description = "Codice fiscale da verificare", required = true) @PathVariable String codiceFiscale) {
        return ResponseEntity.ok(utenteService.codiceFiscaleExists(codiceFiscale));
    }
}