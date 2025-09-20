package com.project.bank_account_management_be.controller;

import com.project.bank_account_management_be.dto.DocumentoIdentitaDTO;
import com.project.bank_account_management_be.service.DocumentoIdentitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/documenti")
@RequiredArgsConstructor
@Tag(name = "Documenti di Identità", description = "Operazioni per la gestione dei documenti di identità")
public class DocumentoIdentitaController {

    private final DocumentoIdentitaService documentoService;

    @Operation(summary = "Recupera tutti i documenti", description = "Restituisce la lista di tutti i documenti di identità")
    @ApiResponse(responseCode = "200", description = "Lista dei documenti recuperata con successo")
    @GetMapping
    public ResponseEntity<List<DocumentoIdentitaDTO>> getAllDocumenti() {
        return ResponseEntity.ok(documentoService.getAllDocumenti());
    }

    @Operation(summary = "Recupera documento per ID", description = "Restituisce i dettagli di un documento specifico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Documento trovato"),
            @ApiResponse(responseCode = "404", description = "Documento non trovato")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoIdentitaDTO> getDocumentoById(
            @Parameter(description = "ID del documento", required = true) @PathVariable Integer id) {
        return ResponseEntity.ok(documentoService.getDocumentoById(id));
    }

    @Operation(summary = "Recupera documenti per utente", description = "Restituisce tutti i documenti di un utente")
    @GetMapping("/utente/{utenteId}")
    public ResponseEntity<List<DocumentoIdentitaDTO>> getDocumentiByUtente(
            @Parameter(description = "ID dell'utente", required = true) @PathVariable Integer utenteId) {
        return ResponseEntity.ok(documentoService.getDocumentiByUtente(utenteId));
    }

    @Operation(summary = "Recupera documenti per codice fiscale", description = "Restituisce tutti i documenti associati a un codice fiscale")
    @GetMapping("/codice-fiscale/{codiceFiscale}")
    public ResponseEntity<List<DocumentoIdentitaDTO>> getDocumentiByCodiceFiscale(
            @Parameter(description = "Codice fiscale dell'utente", required = true) @PathVariable String codiceFiscale) {
        return ResponseEntity.ok(documentoService.getDocumentiByCodiceFiscale(codiceFiscale));
    }

    @Operation(summary = "Crea nuovo documento", description = "Crea un nuovo documento di identità")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Documento creato con successo"),
            @ApiResponse(responseCode = "400", description = "Dati non validi")
    })
    @PostMapping
    public ResponseEntity<DocumentoIdentitaDTO> createDocumento(@Valid @RequestBody DocumentoIdentitaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(documentoService.createDocumento(dto));
    }

    @Operation(summary = "Aggiorna documento", description = "Aggiorna i dati di un documento esistente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Documento aggiornato con successo"),
            @ApiResponse(responseCode = "404", description = "Documento non trovato")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DocumentoIdentitaDTO> updateDocumento(
            @Parameter(description = "ID del documento", required = true) @PathVariable Integer id,
            @Valid @RequestBody DocumentoIdentitaDTO dto) {
        return ResponseEntity.ok(documentoService.updateDocumento(id, dto));
    }

    @Operation(summary = "Elimina documento", description = "Elimina un documento di identità")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Documento eliminato con successo"),
            @ApiResponse(responseCode = "404", description = "Documento non trovato")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumento(
            @Parameter(description = "ID del documento", required = true) @PathVariable Integer id) {
        documentoService.deleteDocumento(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Verifica documenti in scadenza", description = "Restituisce i documenti che scadono nei prossimi 30 giorni")
    @GetMapping("/in-scadenza")
    public ResponseEntity<List<DocumentoIdentitaDTO>> getDocumentiInScadenza() {
        return ResponseEntity.ok(documentoService.getDocumentiInScadenza());
    }

    @Operation(summary = "Verifica documenti scaduti", description = "Restituisce i documenti già scaduti")
    @GetMapping("/scaduti")
    public ResponseEntity<List<DocumentoIdentitaDTO>> getDocumentiScaduti() {
        return ResponseEntity.ok(documentoService.getDocumentiScaduti());
    }
}