package com.project.bank_account_management_be.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class CryptoUtil {

    private static final String HASH_ALGORITHM = "SHA-256";

    /**
     * Cripta una password utilizzando SHA-256
     * @param password Password in chiaro
     * @return Password crittografata in formato esadecimale
     */
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            log.error("Errore nell'algoritmo di hash: ", e);
            throw new RuntimeException("Errore nella crittografia della password", e);
        }
    }

    /**
     * Verifica se una password corrisponde al hash memorizzato
     * @param plainPassword Password in chiaro da verificare
     * @param hashedPassword Hash memorizzato nel database
     * @return true se la password corrisponde, false altrimenti
     */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        String hashedInput = hashPassword(plainPassword);
        return hashedInput.equals(hashedPassword);
    }

    /**
     * Converte un array di byte in una stringa esadecimale
     * @param bytes Array di byte da convertire
     * @return Stringa esadecimale
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    /**
     * Genera un salt casuale per aumentare la sicurezza
     * @return Salt casuale
     */
    public String generateSalt() {
        byte[] salt = new byte[16];
        new java.security.SecureRandom().nextBytes(salt);
        return bytesToHex(salt);
    }

    /**
     * Hash con salt per maggiore sicurezza
     * @param password Password in chiaro
     * @param salt Salt da utilizzare
     * @return Password con hash salato
     */
    public String hashPasswordWithSalt(String password, String salt) {
        return hashPassword(password + salt);
    }
}