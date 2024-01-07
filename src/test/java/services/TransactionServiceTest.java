package services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import repositories.AccountRepository;
import repositories.CategoryRepository;
import repositories.CurrencyRepository;
import repositories.TransactionRepository;

public class TransactionServiceTest {
    private TransactionServices subject = null;

    @BeforeEach
    void setUp(){
        CurrencyRepository currencyRep = new CurrencyRepository ();
        AccountRepository accountRep = new AccountRepository ( currencyRep );
        CategoryRepository categoryRep = new CategoryRepository ();
        TransactionRepository transactionRep = new TransactionRepository (accountRep, categoryRep  );
        subject = new TransactionServices ( accountRep, transactionRep  );
    }

    @AfterEach
    void tearDown(){
      subject = null;
    }
}
