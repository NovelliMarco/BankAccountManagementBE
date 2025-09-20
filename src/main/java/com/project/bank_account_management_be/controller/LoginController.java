package com.project.bank_account_management_be.controller;

import com.project.bank_account_management_be.config.JwtTokenUtil;
import com.project.bank_account_management_be.dto.LoginDTO;
import com.project.bank_account_management_be.dto.LoginResponseDTO;
import com.project.bank_account_management_be.dto.RecuperoPasswordDTO;
import com.project.bank_account_management_be.entity.Utente;
import com.project.bank_account_management_be.service.LoginService;
import com.project.bank_account_management_be.service.RoleValidationService;
import com.project.bank_account_management_be.service.impl.JwtUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Autenticazione", description = "Operazioni per l'autenticazione e recupero password")
public class LoginController {

    private final LoginService loginService;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final RoleValidationService roleValidationService;

    @Operation(summary = "Effettua login",
            description = "Verifica le credenziali utente e restituisce un token JWT. La password deve essere già crittografata con SHA-256 dal frontend")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login effettuato con successo"),
            @ApiResponse(responseCode = "401", description = "Credenziali non valide"),
            @ApiResponse(responseCode = "400", description = "Dati non validi")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        boolean loginSuccessful = loginService.authenticate(loginDTO.getEmail(), loginDTO.getPassword());

        if (!loginSuccessful) {
            return ResponseEntity.status(401).body(LoginResponseDTO.builder()
                    .success(false)
                    .message("Credenziali non valide")
                    .build());
        }

        try {
            // Genera token JWT
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(loginDTO.getEmail());
            String token = jwtTokenUtil.generateToken(userDetails);

            // Ottieni informazioni utente
            Utente utente = jwtUserDetailsService.getUserByEmail(loginDTO.getEmail());
            String ruolo = roleValidationService.getRuoloUtente(utente);

            LoginResponseDTO response = LoginResponseDTO.builder()
                    .success(true)
                    .message("Login effettuato con successo")
                    .token(token)
                    .utenteId(utente.getUtenteId())
                    .email(utente.getEmail())
                    .nome(utente.getNome())
                    .cognome(utente.getCognome())
                    .ruolo(ruolo)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(LoginResponseDTO.builder()
                    .success(false)
                    .message("Errore interno del server")
                    .build());
        }
    }

    @Operation(summary = "Recupero password",
            description = "Avvia procedura di recupero password e restituisce la password temporanea in chiaro e il suo hash")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password temporanea generata e restituita"),
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

    @Operation(summary = "Verifica token",
            description = "Verifica la validità di un token JWT")
    @GetMapping("/verifyToken")
    public ResponseEntity<LoginResponseDTO> verifyToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(LoginResponseDTO.builder()
                        .success(false)
                        .message("Token mancante o formato non valido")
                        .build());
            }

            String token = authHeader.substring(7);
            String email = jwtTokenUtil.getUsernameFromToken(token);

            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(email);

            if (jwtTokenUtil.validateToken(token, userDetails)) {
                Utente utente = jwtUserDetailsService.getUserByEmail(email);
                String ruolo = roleValidationService.getRuoloUtente(utente);

                return ResponseEntity.ok(LoginResponseDTO.builder()
                        .success(true)
                        .message("Token valido")
                        .utenteId(utente.getUtenteId())
                        .email(utente.getEmail())
                        .nome(utente.getNome())
                        .cognome(utente.getCognome())
                        .ruolo(ruolo)
                        .build());
            } else {
                return ResponseEntity.status(401).body(LoginResponseDTO.builder()
                        .success(false)
                        .message("Token non valido o scaduto")
                        .build());
            }

        } catch (Exception e) {
            return ResponseEntity.status(401).body(LoginResponseDTO.builder()
                    .success(false)
                    .message("Token non valido")
                    .build());
        }
    }
}