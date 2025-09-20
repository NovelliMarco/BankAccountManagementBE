package com.project.bank_account_management_be.service.impl;

import com.project.bank_account_management_be.entity.Utente;
import com.project.bank_account_management_be.repository.UtenteRepository;
import com.project.bank_account_management_be.service.RoleValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UtenteRepository utenteRepository;
    private final RoleValidationService roleValidationService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utente utente = utenteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con email: " + email));

        if (utente.getLogin() == null) {
            throw new UsernameNotFoundException("Login non configurato per utente: " + email);
        }

        // Ottieni il ruolo dell'utente
        String ruolo = roleValidationService.getRuoloUtente(utente);

        // Crea le authorities per Spring Security
        Collection<GrantedAuthority> authorities = getAuthorities(ruolo);

        log.debug("Utente autenticato: {} con ruolo: {}", email, ruolo);

        return User.builder()
                .username(email)
                .password(utente.getLogin().getPasswordHash())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    private Collection<GrantedAuthority> getAuthorities(String ruolo) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Aggiungi il ruolo principale
        authorities.add(new SimpleGrantedAuthority("ROLE_" + ruolo));

        // Aggiungi privilegi aggiuntivi basati sul ruolo
        switch (ruolo) {
            case "AMMINISTRATORE":
                authorities.add(new SimpleGrantedAuthority("PRIVILEGE_ALL"));
                authorities.add(new SimpleGrantedAuthority("PRIVILEGE_USER_MANAGEMENT"));
                authorities.add(new SimpleGrantedAuthority("PRIVILEGE_ACCOUNT_MANAGEMENT"));
                authorities.add(new SimpleGrantedAuthority("PRIVILEGE_REPORTING"));
                break;

            case "OPERATORE":
                authorities.add(new SimpleGrantedAuthority("PRIVILEGE_USER_MANAGEMENT"));
                authorities.add(new SimpleGrantedAuthority("PRIVILEGE_ACCOUNT_MANAGEMENT"));
                break;

            case "CLIENTE":
                authorities.add(new SimpleGrantedAuthority("PRIVILEGE_OWN_ACCOUNT_ACCESS"));
                break;

            default:
                // Ruolo di default con privilegi minimi
                authorities.add(new SimpleGrantedAuthority("PRIVILEGE_READ_ONLY"));
                break;
        }

        return authorities;
    }

    /**
     * Metodo helper per ottenere l'utente completo dall'email
     */
    public Utente getUserByEmail(String email) {
        return utenteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con email: " + email));
    }
}