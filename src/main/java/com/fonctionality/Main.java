package com.fonctionality;

import com.fonctionality.entity.*;
import com.fonctionality.repository.AccountRepository;
import com.fonctionality.repository.CurrencyRepository;
import com.fonctionality.repository.TransactionRepository;
import com.fonctionality.service.AccountService;
import com.fonctionality.service.TransactionService;

import java.time.LocalDateTime;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args){

        CurrencyRepository currencyRepository = new CurrencyRepository();
        AccountRepository accountRepository = new AccountRepository(currencyRepository);
        TransactionRepository transactionRepository = new TransactionRepository(accountRepository);
        AccountService accountService = new AccountService ( accountRepository );

        Account account = Account.builder()
                .id(1L)
                .name(AccountName.CURRENT_ACCOUNT)
                .balance(0.0)
                .lastUpdateDateTime(LocalDateTime.now())
                .build();
        Transaction transaction = Transaction.builder()
                .id(6L)
                .amount(1111111.0)
                .label("Ok UPDATE1")
                .account(account)
                .dateTime(LocalDateTime.now())
                .build();

        System.out.println(transactionRepository.update(transaction));
    }


}