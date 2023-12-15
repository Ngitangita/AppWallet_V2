package com.fonctionality;

import com.fonctionality.entity.Account;
import com.fonctionality.repository.AccountRepository;
import com.fonctionality.repository.CurrencyRepository;
import com.fonctionality.repository.TransactionRepository;
import com.fonctionality.service.AccountService;


public class Main {
    public static void main(String[] args){
        CurrencyRepository currencyRepository = new CurrencyRepository();
        AccountRepository accountRepository = new AccountRepository(currencyRepository);
        TransactionRepository transactionRTransaction = new TransactionRepository(accountRepository);
        AccountService accountService = new AccountService ( accountRepository );
        Account debit = accountRepository.findById ( 1L );
        Account credit = accountRepository.findById ( 2L );
        accountService.carryOfTransfer ( credit, debit, 1233 );
        System.out.println(accountRepository.findAll());


    }


}