package com.project.bank_account_management_be.error;

public class SaldoInsufficienteException extends RuntimeException {
    public SaldoInsufficienteException(String message) {
        super(message);
    }
}
