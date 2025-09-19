package com.project.bank_account_management_be.controller;


import com.project.bank_account_management_be.dto.LoginDTO;
import com.project.bank_account_management_be.dto.RecuperoPasswordDTO;
import com.project.bank_account_management_be.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Login", description = "Operazioni per l'autenticazione e recupero password")
public class LoginController {

    private final LoginService loginService;

    @Operation(summary = "Effettua login", description = "Verifica le credenziali utente e restituisce true/false")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login verificato"),
            @ApiResponse(responseCode = "400", description = "Dati non validi")
    })
    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@Valid @RequestBody LoginDTO loginDTO) {
        boolean loginSuccessful = loginService.authenticate(loginDTO.getEmail(), loginDTO.getPassword());
        return ResponseEntity.ok(loginSuccessful);
    }

    @Operation(summary = "Recupero password", description = "Avvia procedura di recupero password tramite dati anagrafici")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Richiesta di recupero password processata"),
            @ApiResponse(responseCode = "400", description = "Dati non validi"),
            @ApiResponse(responseCode = "404", description = "Utente non trovato")
    })
    @PostMapping("/recuperoPassword")
    public ResponseEntity<String> recuperoPassword(@Valid @RequestBody RecuperoPasswordDTO recuperoDTO) {
        String result = loginService.recuperoPassword(
                recuperoDTO.getNome(),
                recuperoDTO.getCognome(),
                recuperoDTO.getCodiceFiscale(),
                recuperoDTO.getEmail()
        );
        return ResponseEntity.ok(result);
    }
}