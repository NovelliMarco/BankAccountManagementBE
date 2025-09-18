package com.project.bank_account_management_be.error;

public class ContoNotFoundException extends RuntimeException {
    public ContoNotFoundException(String message) {
        super(message);
    }
}