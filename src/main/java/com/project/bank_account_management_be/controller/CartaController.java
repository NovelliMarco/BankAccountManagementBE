package com.project.bank_account_management_be.controller;

import com.project.bank_account_management_be.dto.CartaDTO;
import com.project.bank_account_management_be.dto.TransazioneCartaDTO;
import com.project.bank_account_management_be.service.CartaService;
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
import java.util.List;

@RestController
@RequestMapping("/api/carte")
@RequiredArgsConstructor
@Tag(name = "Carte di Pagamento", description = "Operazioni per la gestione delle carte di pagamento")
public class CartaController {

    private final CartaService cartaService;

    @Operation(summary = "Recupera tutte le carte", description = "Restituisce la lista di tutte le carte di pagamento")
    @ApiResponse(responseCode = "200", description = "Lista delle carte recuperata con successo")
    @GetMapping
    public ResponseEntity<List<CartaDTO>> getAll() {
        return ResponseEntity.ok(cartaService.getAllCarte());
    }

    @Operation(summary = "Recupera carta per ID", description = "Restituisce i dettagli di una carta specifica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carta trovata"),
            @ApiResponse(responseCode = "404", description = "Carta non trovata")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CartaDTO> getById(
            @Parameter(description = "ID della carta", required = true) @PathVariable Integer id) {
        return ResponseEntity.ok(cartaService.getCartaById(id));
    }

    @Operation(summary = "Crea nuova carta", description = "Crea una nuova carta di pagamento")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Carta creata con successo"),
            @ApiResponse(responseCode = "400", description = "Dati non validi")
    })
    @PostMapping
    public ResponseEntity<CartaDTO> create(@Valid @RequestBody CartaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartaService.createCarta(dto));
    }

    @Operation(summary = "Aggiorna carta", description = "Aggiorna i dati di una carta esistente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carta aggiornata con successo"),
            @ApiResponse(responseCode = "404", description = "Carta non trovata")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CartaDTO> update(
            @Parameter(description = "ID della carta", required = true) @PathVariable Integer id,
            @Valid @RequestBody CartaDTO dto) {
        return ResponseEntity.ok(cartaService.updateCarta(id, dto));
    }

    @Operation(summary = "Elimina carta", description = "Elimina una carta di pagamento")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Carta eliminata con successo"),
            @ApiResponse(responseCode = "404", description = "Carta non trovata")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID della carta", required = true) @PathVariable Integer id) {
        cartaService.deleteCarta(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Blocca carta", description = "Blocca una carta di pagamento")
    @ApiResponse(responseCode = "200", description = "Carta bloccata con successo")
    @PutMapping("/{id}/blocca")
    public ResponseEntity<CartaDTO> blocca(
            @Parameter(description = "ID della carta", required = true) @PathVariable Integer id) {
        return ResponseEntity.ok(cartaService.bloccaCarta(id));
    }

    @Operation(summary = "Sblocca carta", description = "Sblocca una carta di pagamento precedentemente bloccata")
    @ApiResponse(responseCode = "200", description = "Carta sbloccata con successo")
    @PutMapping("/{id}/sblocca")
    public ResponseEntity<CartaDTO> sblocca(
            @Parameter(description = "ID della carta", required = true) @PathVariable Integer id) {
        return ResponseEntity.ok(cartaService.sbloccaCarta(id));
    }

    @Operation(summary = "Recupera carte per codice fiscale", description = "Restituisce tutte le carte associate a un codice fiscale")
    @GetMapping("/utente/{codiceFiscale}")
    public ResponseEntity<List<CartaDTO>> getCarteByCodiceFiscale(
            @Parameter(description = "Codice fiscale dell'utente", required = true) @PathVariable String codiceFiscale) {
        return ResponseEntity.ok(cartaService.getCarteByCodiceFiscale(codiceFiscale));
    }

    @Operation(summary = "Recupera carte per nome e cognome", description = "Restituisce tutte le carte associate a nome e cognome")
    @GetMapping("/utente")
    public ResponseEntity<List<CartaDTO>> getCarteByNomeECognome(
            @Parameter(description = "Nome dell'utente", required = true) @RequestParam String nome,
            @Parameter(description = "Cognome dell'utente", required = true) @RequestParam String cognome) {
        return ResponseEntity.ok(cartaService.getCarteByNomeECognome(nome, cognome));
    }

    @Operation(summary = "Conta carte per codice fiscale", description = "Restituisce il numero di carte associate a un codice fiscale")
    @GetMapping("/utente/{codiceFiscale}/count")
    public ResponseEntity<Long> countCarteByCodiceFiscale(
            @Parameter(description = "Codice fiscale dell'utente", required = true) @PathVariable String codiceFiscale) {
        return ResponseEntity.ok(cartaService.countCarteByCodiceFiscale(codiceFiscale));
    }

    @Operation(summary = "Recupera transazioni della carta", description = "Restituisce le transazioni di una carta con paginazione")
    @GetMapping("/{id}/transazioni")
    public ResponseEntity<Page<TransazioneCartaDTO>> getTransazioniCarta(
            @Parameter(description = "ID della carta", required = true) @PathVariable Integer id,
            @PageableDefault(size = 10, sort = "dataOperazione", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(cartaService.getTransazioniCarta(id, pageable));
    }
}