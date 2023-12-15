package com.fonctionality;

import com.fonctionality.entity.CodeCurrency;
import com.fonctionality.entity.Currency;
import com.fonctionality.entity.NameCurrency;
import com.fonctionality.repository.AccountRepository;
import com.fonctionality.repository.CurrencyRepository;
import com.fonctionality.repository.TransactionRepository;

import java.util.List;

public class Main {
    public static void main(String[] args){
        CurrencyRepository currencyRepository = new CurrencyRepository();
        AccountRepository accountRepository = new AccountRepository(currencyRepository);
        TransactionRepository transactionRTransaction = new TransactionRepository(accountRepository);
        System.out.println(accountRepository.findAll());

    }


}