package com.fonctionality;

import com.fonctionality.entity.Account;
import com.fonctionality.repository.AccountRepository;
import com.fonctionality.repository.CurrencyRepository;
import com.fonctionality.repository.TransactionRepository;
import com.fonctionality.service.AccountService;
import com.fonctionality.service.TransactionService;

import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args){
        CurrencyRepository currencyRepository = new CurrencyRepository();
        AccountRepository accountRepository = new AccountRepository(currencyRepository);
        TransactionRepository transactionRepository = new TransactionRepository(accountRepository);
        AccountService accountService = new AccountService ( accountRepository );
        TransactionService transactionService = new TransactionService ( transactionRepository );
        Account debit = accountRepository.findById ( 3L );
        Account credit = accountRepository.findById ( 4L );
        System.out.println(accountService.carryOfTransfer(credit, debit, 203));
        System.out.println ( transactionService.getSoldWithDate ( LocalDateTime.parse ( "2023-12-01 00:15:00" ) ));
    }


}