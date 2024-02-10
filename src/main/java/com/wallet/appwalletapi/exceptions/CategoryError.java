package com.wallet.appwalletapi.exceptions;

public class CategoryError extends RuntimeException{
    public CategoryError(String message){
        super ( message );
    }
}
