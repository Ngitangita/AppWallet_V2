package repositories;


import entitries.*;
import exceptions.AccountError;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
public class AccountTest {
    private AccountRepository subject = null;


    @BeforeEach
    void setUp() {
        CurrencyRepository currencyRep = new CurrencyRepository ( );
        subject = new AccountRepository ( currencyRep );
    }


    @Test
    void testFindAllAccountSuccess() {
        assertDoesNotThrow ( () -> {
            int size = 11;
            List<Account> accounts = subject.findAll();
            assertNotNull(accounts, "List of accounts should not be null");
            assertFalse(accounts.isEmpty(), "List of accounts should not be empty");
            assertEquals(size, accounts.size(),  "List should contain four accounts");
        } );
    }


    @Test
    void testFindByIdAccountCurrencySuccess() {
        assertDoesNotThrow ( () -> {
            Long id = 1L;
            Account account = subject.findById (id);
            assertNotNull(account.getId (), "Account id should not be null");
            assertEquals ( AccountName.CURRENT_ACCOUNT, account.getName ( ), "The name of accounts should be equals" );
            assertNotNull (account.getCurrency (),  "the currency  of the shouldAccount should be equals");
        } );
    }


    @Test
    void testSaveAllAccountWithoutCurrencySuccess() {
        assertDoesNotThrow(() -> {
            List<Account> accountsToSave = List.of(
                    Account.builder()
                            .name(AccountName.SAVINGS_ACCOUNT)
                            .account_type(TypeAccount.CASH)
                            .lastUpdateDateTime(LocalDateTime.now())
                            .balance(15.00)
                            .build()
            );

            List<Account> savedAccounts = subject.saveAll(accountsToSave);
            assertNotNull(savedAccounts.get(0).getId(), "Account id should not be null");
            assertNull(savedAccounts.get ( 0 ).getCurrency (), "Currency should be null");
            assertEquals(accountsToSave.get(0).getName(), savedAccounts.get(0).getName(), "The name of the account should be equals");
        });
    }


    @Test
    void testUpdateAllAccountWithoutCurrencySuccess() {
        assertDoesNotThrow(() -> {
            final Long id = 18L;
            List<Account> accountsToSave = List.of(
                    Account.builder()
                            .id ( id )
                            .name(AccountName.CURRENT_ACCOUNT)
                            .account_type(TypeAccount.CASH)
                            .lastUpdateDateTime(LocalDateTime.now())
                            .balance(15.00)
                            .build()
            );

            List<Account> savedAccounts = subject.updateAll (accountsToSave);
            assertNotNull(savedAccounts.get(0).getId(), "Account id should not be null");
            assertNull(savedAccounts.get ( 0 ).getCurrency (), "Currency should be null");
            assertEquals(accountsToSave.get(0).getName(), savedAccounts.get(0).getName(), "The name of the account should be not equals");
        });
    }

    @Test
    void testUpdateAllAccountWithCurrencySuccess() {
        assertDoesNotThrow(() -> {
            Currency currency = new Currency ( 5L, NameCurrency.EURO, CodeCurrency.EUR );
            final Long id = 5L;
            List<Account> accountsToUpdate = List.of(
                    Account.builder()
                            .id ( id )
                            .name(AccountName.CURRENT_ACCOUNT)
                            .account_type(TypeAccount.CASH)
                            .lastUpdateDateTime(LocalDateTime.now())
                            .currency ( currency )
                            .balance(15.00)
                            .build()
            );

            List<Account> updatedAccounts = subject.updateAll (accountsToUpdate);
            assertNotNull(updatedAccounts.get(0).getId(), "Account id should not be null");
            assertNotNull(updatedAccounts.get ( 0 ).getCurrency (), "Currency should be not null");
            assertEquals(accountsToUpdate.get(0).getName(), updatedAccounts.get(0).getName(), "The name of the account should be not equals");
        });
    }

    @Test
    void testSaveAllAccountWithCurrencySuccess() {
        assertDoesNotThrow(() -> {
            Currency currency = new Currency ( null, NameCurrency.EURO, CodeCurrency.EUR );
            List<Account> accountsToSave = List.of(
                    Account.builder()
                            .name(AccountName.SAVINGS_ACCOUNT)
                            .account_type(TypeAccount.CASH)
                            .lastUpdateDateTime(LocalDateTime.now())
                            .balance(15.00)
                            .currency ( currency )
                            .build()
            );

            List<Account> savedAccounts = subject.saveAll(accountsToSave);
            assertNotNull(savedAccounts.get(0).getId(), "Account id should not be null");
            assertNotNull(savedAccounts.get ( 0 ).getCurrency (), "Currency should be null");
            assertEquals(accountsToSave.get(0).getName(), savedAccounts.get(0).getName(), "The name of the account should be equals");
        });
    }


    @Test
    void testDeleteByIdCurrencySuccess() {
        assertDoesNotThrow ( () -> {
            Long id = 18L;
            Account accountBeforeDeleted = subject.findById ( id );
            List<Account> accountsBeforeDeleted = subject.findAll();
           Account accountAfterDeleted = subject.deleteById ( id );
            List<Account> accountsAfterDeleted = subject.findAll();

            assertEquals (accountAfterDeleted.getId (),accountBeforeDeleted.getId () , "the two id should  be equals");
            assertNotEquals (accountsAfterDeleted.size () , accountsBeforeDeleted.size (),  "the size of two list  should be not equals");
        } );
    }



    @Test
    void testAccountSQLException() {
        AccountError exception = assertThrows( AccountError.class, () -> {
            throw new AccountError ( "Error retrieving accounts from database" );
        });
        assertEquals("Error retrieving accounts from database", exception.getMessage(), "should be equal");
    }


    @AfterEach
    void tearDown() {
        subject = null;
    }
}
