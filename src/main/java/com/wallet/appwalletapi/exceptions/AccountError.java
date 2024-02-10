package com.wallet.appwalletapi.exceptions;

public class AccountError extends RuntimeException {

    public AccountError (String message) {
        super(message);
    }
}
