package com.project.bank_account_management_be.controller;

import com.project.bank_account_management_be.dto.CartaDTO;
import com.project.bank_account_management_be.dto.TransazioneCartaDTO;
import com.project.bank_account_management_be.service.CartaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carte")
@RequiredArgsConstructor
public class CartaController {

    private final CartaService cartaService;

    @GetMapping
    public ResponseEntity<List<CartaDTO>> getAll() {
        return ResponseEntity.ok(cartaService.getAllCarte());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartaDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(cartaService.getCartaById(id));
    }

    @PostMapping
    public ResponseEntity<CartaDTO> create(@RequestBody CartaDTO dto) {
        return ResponseEntity.ok(cartaService.createCarta(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartaDTO> update(@PathVariable Integer id, @RequestBody CartaDTO dto) {
        return ResponseEntity.ok(cartaService.updateCarta(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        cartaService.deleteCarta(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/blocca")
    public ResponseEntity<CartaDTO> blocca(@PathVariable Integer id) {
        return ResponseEntity.ok(cartaService.bloccaCarta(id));
    }

    @PutMapping("/{id}/sblocca")
    public ResponseEntity<CartaDTO> sblocca(@PathVariable Integer id) {
        return ResponseEntity.ok(cartaService.sbloccaCarta(id));
    }

    @GetMapping("/utente/{codiceFiscale}")
    public ResponseEntity<List<CartaDTO>> getCarteByCodiceFiscale(@PathVariable String codiceFiscale) {
        return ResponseEntity.ok(cartaService.getCarteByCodiceFiscale(codiceFiscale));
    }

    @GetMapping("/utente")
    public ResponseEntity<List<CartaDTO>> getCarteByNomeECognome(
            @RequestParam String nome,
            @RequestParam String cognome) {
        return ResponseEntity.ok(cartaService.getCarteByNomeECognome(nome, cognome));
    }

    @GetMapping("/utente/{codiceFiscale}/count")
    public ResponseEntity<Long> countCarteByCodiceFiscale(@PathVariable String codiceFiscale) {
        return ResponseEntity.ok(cartaService.countCarteByCodiceFiscale(codiceFiscale));
    }

    @GetMapping("/{id}/transazioni")
    public ResponseEntity<Page<TransazioneCartaDTO>> getTransazioniCarta(  @PathVariable Integer id,  Pageable pageable) {
        return ResponseEntity.ok(cartaService.getTransazioniCarta(id, pageable));
    }

}
