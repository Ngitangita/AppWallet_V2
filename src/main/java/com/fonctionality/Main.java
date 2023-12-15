package com.fonctionality;

import com.fonctionality.entity.*;
import com.fonctionality.repository.AccountRepository;
import com.fonctionality.repository.CurrencyRepository;
import com.fonctionality.repository.TransactionRepository;
import com.fonctionality.repository.TransferHistoryRepository;
import com.fonctionality.service.AccountService;

import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args){

        CurrencyRepository currencyRepository = new CurrencyRepository();
        AccountRepository accountRepository = new AccountRepository(currencyRepository);
        TransactionRepository transactionRepository = new TransactionRepository(accountRepository);
        TransferHistoryRepository transferHistoryRepository = new TransferHistoryRepository();
        AccountService accountService = new AccountService ( accountRepository , transferHistoryRepository);

        Account account1 =accountRepository.findById(10L);
        Account account2 = accountRepository.findById(2L);
        Transaction transaction = Transaction.builder()
                .id(6L)
                .transactionType(TypeTransaction.CREDIT)
                .amount(1111111.0)
                .label("Ok UPDATE1")
                .dateTime(LocalDateTime.now())
                .build();

        System.out.println(accountService.carryOfTransfer(account2, account1, 100000000000.0));
    }


}