package com.project.bank_account_management_be.controller;

import com.project.bank_account_management_be.dto.BonificoDTO;
import com.project.bank_account_management_be.dto.ContoDTO;
import com.project.bank_account_management_be.dto.StoricoMovimentoContoDTO;
import com.project.bank_account_management_be.dto.TransazioneContoDTO;
import com.project.bank_account_management_be.service.ContoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/conti")
@RequiredArgsConstructor
@Tag(name = "Conti Bancari - Cliente", description = "Operazioni sui conti per i clienti (lettura e operazioni base)")
public class ContoController {

    private final ContoService contoService;

    @Operation(summary = "Recupera conto per ID", description = "Restituisce i dettagli di un conto specifico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conto trovato"),
            @ApiResponse(responseCode = "404", description = "Conto non trovato")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContoDTO> getContoById(
            @Parameter(description = "ID del conto", required = true) @PathVariable Integer id) {
        return ResponseEntity.ok(contoService.getContoById(id));
    }

    @Operation(summary = "Recupera conto per IBAN", description = "Restituisce i dettagli di un conto tramite IBAN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conto trovato"),
            @ApiResponse(responseCode = "404", description = "Conto non trovato")
    })
    @GetMapping("/iban/{iban}")
    public ResponseEntity<ContoDTO> getContoByIban(
            @Parameter(description = "IBAN del conto", required = true) @PathVariable String iban) {
        return ResponseEntity.ok(contoService.getContoByIban(iban));
    }

    @Operation(summary = "Deposita denaro", description = "Effettua un deposito sul conto")
    @ApiResponse(responseCode = "200", description = "Deposito effettuato con successo")
    @PostMapping("/{id}/deposito")
    public ResponseEntity<ContoDTO> deposito(
            @Parameter(description = "ID del conto", required = true) @PathVariable Integer id,
            @Parameter(description = "Importo da depositare", required = true) @RequestParam BigDecimal importo,
            @Parameter(description = "Descrizione del deposito") @RequestParam(required = false) String descrizione) {
        return ResponseEntity.ok(contoService.deposito(id, importo, descrizione));
    }

    @Operation(summary = "Preleva denaro", description = "Effettua un prelievo dal conto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prelievo effettuato con successo"),
            @ApiResponse(responseCode = "400", description = "Saldo insufficiente")
    })
    @PostMapping("/{id}/prelievo")
    public ResponseEntity<ContoDTO> prelievo(
            @Parameter(description = "ID del conto", required = true) @PathVariable Integer id,
            @Parameter(description = "Importo da prelevare", required = true) @RequestParam BigDecimal importo,
            @Parameter(description = "Descrizione del prelievo") @RequestParam(required = false) String descrizione) {
        return ResponseEntity.ok(contoService.prelievo(id, importo, descrizione));
    }

    @Operation(summary = "Effettua bonifico", description = "Effettua un bonifico da un conto a un altro")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bonifico effettuato con successo"),
            @ApiResponse(responseCode = "400", description = "Saldo insufficiente o dati non validi")
    })
    @PostMapping("/{id}/bonifico")
    public ResponseEntity<String> bonifico(
            @Parameter(description = "ID del conto mittente", required = true) @PathVariable Integer id,
            @Valid @RequestBody BonificoDTO bonifico) {
        contoService.bonifico(id, bonifico);
        return ResponseEntity.ok("Bonifico effettuato con successo");
    }

    @Operation(summary = "Recupera conti per codice fiscale", description = "Restituisce tutti i conti associati a un codice fiscale")
    @GetMapping("/utente/{codiceFiscale}")
    public ResponseEntity<List<ContoDTO>> getContiByCodiceFiscale(
            @Parameter(description = "Codice fiscale dell'utente", required = true) @PathVariable String codiceFiscale) {
        return ResponseEntity.ok(contoService.getContiByCodiceFiscale(codiceFiscale));
    }

    @Operation(summary = "Recupera conti per nome e cognome", description = "Restituisce tutti i conti associati a nome e cognome")
    @GetMapping("/utente")
    public ResponseEntity<List<ContoDTO>> getContiByNomeECognome(
            @Parameter(description = "Nome dell'utente", required = true) @RequestParam String nome,
            @Parameter(description = "Cognome dell'utente", required = true) @RequestParam String cognome) {
        return ResponseEntity.ok(contoService.getContiByNomeECognome(nome, cognome));
    }

    @Operation(summary = "Recupera transazioni del conto", description = "Restituisce le transazioni di un conto con paginazione")
    @GetMapping("/{id}/transazioni")
    public ResponseEntity<Page<TransazioneContoDTO>> getTransazioniConto(
            @Parameter(description = "ID del conto", required = true) @PathVariable Integer id,
            @PageableDefault(size = 10, sort = "dataOperazione", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(contoService.getTransazioniConto(id, pageable));
    }

    @Operation(summary = "Recupera movimenti del conto", description = "Restituisce lo storico dei movimenti di un conto con paginazione")
    @GetMapping("/{id}/movimenti")
    public ResponseEntity<Page<StoricoMovimentoContoDTO>> getMovimentiConto(
            @Parameter(description = "ID del conto", required = true) @PathVariable Integer id,
            @PageableDefault(size = 10, sort = "dataMovimento", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(contoService.getMovimentiConto(id, pageable));
    }

    @Operation(summary = "Controlla esistenza IBAN", description = "Verifica se un IBAN esiste già nel sistema")
    @GetMapping("/iban/{iban}/exists")
    public ResponseEntity<Boolean> checkIbanExists(
            @Parameter(description = "IBAN da verificare", required = true) @PathVariable String iban) {
        return ResponseEntity.ok(contoService.ibanExists(iban));
    }
}