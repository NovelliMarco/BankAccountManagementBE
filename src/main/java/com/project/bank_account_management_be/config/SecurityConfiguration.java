package com.project.bank_account_management_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfiguration(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                 JwtRequestFilter jwtRequestFilter) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .authorizeHttpRequests(authz -> authz
                        // Endpoint pubblici
                        .antMatchers("/api/login", "/api/recuperoPassword").permitAll()
                        .antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                        .antMatchers("/actuator/health").permitAll()

                        // Endpoint amministrativi - solo AMMINISTRATORE e OPERATORE
                        .antMatchers("/api/admin/**").hasAnyRole("AMMINISTRATORE", "OPERATORE")

                        // Creazione utenti - solo operatori
                        .antMatchers(HttpMethod.POST, "/api/utenti").hasAnyRole("AMMINISTRATORE", "OPERATORE")

                        // Operazioni sui conti - clients possono solo consultare e operare sui propri
                        .antMatchers(HttpMethod.GET, "/api/conti/**").authenticated()
                        .antMatchers(HttpMethod.POST, "/api/conti/*/deposito").authenticated()
                        .antMatchers(HttpMethod.POST, "/api/conti/*/prelievo").authenticated()
                        .antMatchers(HttpMethod.POST, "/api/conti/*/bonifico").authenticated()

                        // Creazione, modifica, eliminazione conti - solo operatori
                        .antMatchers(HttpMethod.POST, "/api/conti").hasAnyRole("AMMINISTRATORE", "OPERATORE")
                        .antMatchers(HttpMethod.PUT, "/api/conti/**").hasAnyRole("AMMINISTRATORE", "OPERATORE")
                        .antMatchers(HttpMethod.DELETE, "/api/conti/**").hasAnyRole("AMMINISTRATORE", "OPERATORE")

                        // Operazioni sulle carte
                        .antMatchers(HttpMethod.GET, "/api/carte/**").authenticated()
                        .antMatchers(HttpMethod.POST, "/api/carte").hasAnyRole("AMMINISTRATORE", "OPERATORE")
                        .antMatchers(HttpMethod.PUT, "/api/carte/**").hasAnyRole("AMMINISTRATORE", "OPERATORE")
                        .antMatchers(HttpMethod.DELETE, "/api/carte/**").hasAnyRole("AMMINISTRATORE", "OPERATORE")

                        // Gestione utenti
                        .antMatchers(HttpMethod.GET, "/api/utenti/search").authenticated()
                        .antMatchers(HttpMethod.GET, "/api/utenti/*/exists").authenticated()
                        .antMatchers(HttpMethod.GET, "/api/utenti/**").authenticated()
                        .antMatchers(HttpMethod.PUT, "/api/utenti/**").hasAnyRole("AMMINISTRATORE", "OPERATORE")
                        .antMatchers(HttpMethod.DELETE, "/api/utenti/**").hasAnyRole("AMMINISTRATORE", "OPERATORE")

                        // Tutto il resto richiede autenticazione
                        .anyRequest().authenticated()
                )
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Aggiungi il filtro JWT
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "X-Operator-Id"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}