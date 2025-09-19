package com.project.bank_account_management_be.service;

public interface LoginService {

    boolean authenticate(String email, String hashedPassword);

    String recuperoPassword(String nome, String cognome, String codiceFiscale, String email);
}