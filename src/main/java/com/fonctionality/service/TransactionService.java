package com.fonctionality.service;


import com.fonctionality.dto.response.SoldWithDate;
import com.fonctionality.entity.Account;
import com.fonctionality.entity.Transaction;
import com.fonctionality.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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


    public List<SoldWithDate> getBalanceHistory(Account account, LocalDateTime startDate, LocalDateTime endDate) {
        List<SoldWithDate> balanceHistories =  new ArrayList<>();
        List<Transaction> transactions = this.transactionRepository.findAll();
        for (Transaction transaction : transactions) {
            if (transaction.getDateTime().isBefore (  startDate)
                    && transaction.getDateTime() .isAfter (  endDate )
                    && Objects.equals(transaction.getAccount().getId(), account.getId())
            ){
                SoldWithDate balanceHistory = new SoldWithDate(
                        transaction.getDateTime(),
                        transaction.getAmount()
                );
                balanceHistories.add(balanceHistory);
            }
        }
            return balanceHistories;
    }
}

