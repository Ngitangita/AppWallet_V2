package repositories;

import entitries.*;
import exceptions.TransactionError;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    private TransactionRepository subject = null;


    @BeforeEach
    void setUp() {
        CurrencyRepository currencyRep = new CurrencyRepository ();
        AccountRepository accountRep = new AccountRepository ( currencyRep);
        subject = new TransactionRepository ( accountRep );
    }


    @Test
    void testFindAllTransactionsSuccess() {
        assertDoesNotThrow ( () -> {
            int size = 4;
            List<Transaction> transactions = subject.findAll();
            assertNotNull(transactions, "List of transactions should not be null");
            assertFalse(transactions.isEmpty(), "List of transactions should not be empty");
            assertEquals(size, transactions.size(),  "List should contain four transactions");
        } );
    }


    @Test
    void testFindByIdTransactionSuccess() {
        assertDoesNotThrow ( () -> {
            Long id = 5L;
            Transaction transaction = subject.findById (id);
            assertNotNull(transaction.getId (), "Account id should not be null");
            assertEquals ( TypeTransaction.CREDIT, transaction.getTypeTransaction ( ), "The type of transaction  should be equals" );
            assertNotNull (transaction.getAccount (),  "the account of transaction should be not null");
        } );
    }


    @Test
     void testSaveTransactionSuccess() {
        Transaction transaction = Transaction.builder()
                .label("Sample Transaction")
                .amount(100.0)
                .dateTime(LocalDateTime.now())
                .typeTransaction(TypeTransaction.CREDIT)
                .build();
        Transaction savedTransaction = subject.save(transaction);
        assertNotNull(savedTransaction.getId(), "Transaction id should not be null");
    }


    @Test
    void testSaveTransactionWithAccountSuccess() {
        Transaction transaction = Transaction.builder()
                .label("try Transaction")
                .amount(100.0)
                .dateTime(LocalDateTime.now())
                .account ( Account.builder()
                        .name( AccountName.CURRENT_ACCOUNT)
                        .account_type( TypeAccount.CASH)
                        .lastUpdateDateTime(LocalDateTime.now())
                        .balance(15.00)
                        .build() )
                .typeTransaction(TypeTransaction.CREDIT)
                .build();
        Transaction savedTransaction = subject.save(transaction);
        assertNotNull(savedTransaction.getId(), "Transaction id should not be null");
        assertNotNull(savedTransaction.getAccount ().getId(), "Account id should not be null");
    }


    @Test
    void testUpdateTransactionSuccess() {
        Long id = 15L;
        Transaction transaction = Transaction.builder()
                .id(id)
                .label("Transaction")
                .amount(100.0)
                .dateTime(LocalDateTime.now())
                .typeTransaction(TypeTransaction.DEBIT)
                .build();
        Transaction savedTransaction = subject.update (transaction);
        assertEquals (savedTransaction.getId(), transaction.getId (), "The two id of transaction should  be equal");
        assertEquals (savedTransaction.getTypeTransaction (), transaction.getTypeTransaction (), "The name of two transaction should  be not equal");
    }

    @Test
    void testUpdateTransactionWithAccountSuccess() {
        Long id = 17L;
        Long accountId = 17L;
        Transaction transaction = Transaction.builder()
                .id(id)
                .label("Update Transaction")
                .amount(100.0)
                .dateTime(LocalDateTime.now())
                .account ( Account.builder()
                        .id ( accountId )
                        .name(AccountName.CURRENT_ACCOUNT)
                        .account_type(TypeAccount.BANK)
                        .lastUpdateDateTime(LocalDateTime.now())
                        .balance(15.00)
                        .build() )
                .typeTransaction(TypeTransaction.DEBIT)
                .build();
        Transaction savedTransaction = subject.update (transaction);
        assertEquals (savedTransaction.getId(), transaction.getId (), "The two id of transaction should  be equal");
        assertEquals (savedTransaction.getTypeTransaction (), transaction.getTypeTransaction (), "The name of two transaction should  be not equal");
    }


    @Test
    void testDeleteByIdTransactionSuccess() {
        assertDoesNotThrow ( () -> {
            Long id = 12L;
            Transaction transactionBeforeDeleted = subject.findById ( id );
            List<Transaction> transactionsBeforeDeleted = subject.findAll();
            Transaction transactionAfterDeleted = subject.deleteById ( id );
            List<Transaction>transactionsAfterDeleted = subject.findAll();

            assertEquals (transactionAfterDeleted.getId (),transactionBeforeDeleted.getId () , "the two id should  be equals");
            assertNotEquals (transactionsAfterDeleted.size () ,transactionsBeforeDeleted.size (),  "the size of two list  should be not equals");
        } );
    }

    @Test
    void testTransactionSQLException() {
        TransactionError exception = assertThrows(TransactionError.class, () -> {
            throw new TransactionError ( "Error retrieving currencies from database" );
        });
        assertEquals("Error retrieving currencies from database", exception.getMessage(), "should be equal");
    }

    @AfterEach
    void tearDown() {
        subject = null;
    }

}
