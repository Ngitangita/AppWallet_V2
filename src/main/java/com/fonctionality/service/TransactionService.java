package com.fonctionality.service;


import com.fonctionality.dto.response.SoldWithDate;
import com.fonctionality.entity.Account;
import com.fonctionality.entity.Transaction;
import com.fonctionality.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;

public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    public SoldWithDate getSoldWithDate(LocalDateTime dateTime) {
        List<Transaction> transactions = this.transactionRepository.findAll ();
        Transaction find = transactions.stream( )
                .filter ( transaction -> transaction.getDateTime ().equals (   dateTime) )
                .toList ( ).get ( 0 );
        return new SoldWithDate (
                dateTime,
                find.getAmount ()
        );
    }


}

