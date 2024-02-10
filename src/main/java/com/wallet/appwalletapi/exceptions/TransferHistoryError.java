package com.wallet.appwalletapi.exceptions;

public class TransferHistoryError extends RuntimeException{
    public TransferHistoryError(String message){
        super ( message );
    }
}
