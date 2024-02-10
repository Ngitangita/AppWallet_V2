package com.wallet.appwalletapi.services;

import com.wallet.appwalletapi.entitries.Account;
import com.wallet.appwalletapi.entitries.Transaction;
import com.wallet.appwalletapi.entitries.TypeTransaction;
import com.wallet.appwalletapi.services.TransactionServices;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.wallet.appwalletapi.repositories.AccountRepository;
import com.wallet.appwalletapi.repositories.CategoryRepository;
import com.wallet.appwalletapi.repositories.CurrencyRepository;
import com.wallet.appwalletapi.repositories.TransactionRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PerformTransactionServiceTest {
    private TransactionServices subject;
    private AccountRepository accountRep ;
    private TransactionRepository transactionRep;

    @BeforeEach
    void setUp(){
        CurrencyRepository currencyRep = new CurrencyRepository ();
        accountRep = new AccountRepository ( currencyRep );
        CategoryRepository categoryRep = new CategoryRepository ();
        transactionRep = new TransactionRepository (accountRep, categoryRep  );
        subject = new TransactionServices ( accountRep, transactionRep  );
    }

    @Test
    void performTransactionDebitWithInsufficientBalance() {

    }

    @Test
    void performTransactionDebitWithSufficientBalance() {
        Long accountId = 3L;
        double amount = 15;
        Account account = this.accountRep.findById ( accountId );
        List<Transaction> beforeDebitTransactions = this.transactionRep.findAll ();
        Account res = this.subject.creditOrDebitSold ( accountId, TypeTransaction.DEBIT, amount,"Transaction" );
        List<Transaction> afterDebitTransactions = this.transactionRep.findAll ();
        assertNotEquals ( afterDebitTransactions.size(), beforeDebitTransactions.size (), "The size of two list should be not equals"  );
        assertNotEquals ( account.getBalance (), res.getBalance (), "The two sold  should be not equals"  );
    }

    @Test
    void performTransactionCreditSufficientBalance() {
        Long accountId = 3L;
        double amount = 30;
        Account account = this.accountRep.findById ( accountId );
        List<Transaction> beforeDebitTransactions = this.transactionRep.findAll ();
        Account res = this.subject.creditOrDebitSold ( accountId, TypeTransaction.CREDIT, amount,"credit Transaction" );
        List<Transaction> afterDebitTransactions = this.transactionRep.findAll ();
        assertNotEquals ( afterDebitTransactions.size(), beforeDebitTransactions.size (), "The size of two list should be not equals"  );
        assertNotEquals ( account.getBalance (), res.getBalance (), "The two sold  should be not equals"  );
    }


    @AfterEach
    void tearDown(){
      accountRep = null;
      transactionRep = null;
      subject = null;
    }
}
