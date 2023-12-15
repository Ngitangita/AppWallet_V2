package com.fonctionality;

import com.fonctionality.repository.AccountRepository;
import com.fonctionality.repository.CurrencyRepository;
import com.fonctionality.repository.TransactionRepository;


public class Main {
    public static void main(String[] args){
        CurrencyRepository currencyRepository = new CurrencyRepository();
        AccountRepository accountRepository = new AccountRepository(currencyRepository);
        TransactionRepository transactionRTransaction = new TransactionRepository(accountRepository);
        System.out.println(accountRepository.findAll());

    }


}