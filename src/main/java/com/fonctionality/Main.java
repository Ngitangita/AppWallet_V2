package com.fonctionality;

import com.fonctionality.entity.*;
import com.fonctionality.repository.AccountRepository;
import com.fonctionality.repository.CurrencyRepository;
import com.fonctionality.repository.TransactionRepository;
import com.fonctionality.service.AccountService;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;


public class Main {
    public static void main(String[] args){

        Currency currency = Currency.builder()
                .id(3L)
                .code(CodeCurrency.AR)
                .name(NameCurrency.ARIARY)
                .build();
        CurrencyRepository currencyRepository = new CurrencyRepository();
        AccountRepository accountRepository = new AccountRepository(currencyRepository);
        TransactionRepository transactionRTransaction = new TransactionRepository(accountRepository);
        AccountService accountService = new AccountService ( accountRepository );
        System.out.println(accountRepository.deleteById(11L));

    }


}