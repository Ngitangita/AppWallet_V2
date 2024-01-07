package repositories;

import entitries.CodeCurrency;
import entitries.Currency;
import entitries.NameCurrency;
import exceptions.CurrencyError;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CurrencyTest {
    private CurrencyRepository subject = null;

    @BeforeEach
    void setUp() {
        subject = new CurrencyRepository();
    }


    @Test
    void testFindAllCurrencySuccess() {
        assertDoesNotThrow ( () -> {
            List<Currency> currencies = subject.findAll();
            assertNotNull(currencies, "List of currencies should not be null");
            assertFalse(currencies.isEmpty(), "List of currencies should not be empty");
            assertEquals(2, currencies.size(),  "List should contain two currencies");
        } );
    }


    @Test
    void testFindByIdCurrencySuccess() {
        assertDoesNotThrow ( () -> {
            Long id = 1L;
            Currency shouldCurrency = new Currency ( id, NameCurrency.EURO, CodeCurrency.EUR );
            Currency currency = subject.findById (id);
            List<Currency> currencies = subject.findAll();

            assertNotNull(currency.getId (), "currency id should not be null");
            assertTrue (currencies.contains ( currency ), "List of currencies should contain currency");
            assertEquals(shouldCurrency, currency,  "the currency  of the shouldCurrency should be equals");
        } );
    }


        @Test
        void testSaveCurrencySuccess() {
            assertDoesNotThrow ( () -> {
                Currency currency = new Currency ( null, NameCurrency.ARIARY, CodeCurrency.AR );
                Currency currencySaved = subject.save ( currency );
                List<Currency> currencies = subject.findAll();
                assertNotNull(currencySaved.getId (), "currency id should not be null");
                assertTrue (currencies.contains ( currencySaved ), "List of currencies should contain currencySaved");
                assertEquals(currency.getName (), currencySaved.getName (),  "the name of currency and the name of currencySave should be equals");
                assertEquals(currency.getCode (), currencySaved.getCode (),  "the code of currency and the code of currencySave should be equals");
            } );
        }


    @Test
    void testUpdateByIdCurrencySuccess() {
        assertDoesNotThrow ( () -> {
            Long id = 2L;
            Currency currencyBeforeUpdated = subject.findById ( id );
            Currency currency = subject.findById ( id );
            currency.setName ( NameCurrency.EURO );
            currency.setCode ( CodeCurrency.EUR );
            Currency currencyAfterUpdated = subject.update ( currency );
            List<Currency> currencies = subject.findAll();

            assertEquals (currency.getId (),currencyAfterUpdated.getId () , "the two id should  be equals");
            assertTrue (currencies.contains ( currencyAfterUpdated ), "List of currencies should contain currencyAfterUpdated");
            assertNotEquals (currencyBeforeUpdated, currencyAfterUpdated,  "the two currency  should be not equals");
        } );
    }


    @Test
    void testDeleteByIdCurrencySuccess() {
        assertDoesNotThrow ( () -> {
            Long id = 4L;
            Currency currencyBeforeDelete = subject.findById ( id );
            List<Currency> currenciesBeforeDeleted = subject.findAll();
            Currency currencyDeleted = subject.deleteById ( id );
            List<Currency> currenciesAfterDeleted = subject.findAll();

            assertEquals (currencyBeforeDelete.getId (),currencyDeleted.getId () , "the two id should  be equals");
            assertFalse (currenciesAfterDeleted.contains ( currencyDeleted ), "List of currencies should contain currencyAfterUpdated");
            assertNotEquals (currenciesAfterDeleted.size () , currenciesBeforeDeleted.size (),  "the size of two list  should be not equals");
        } );
    }


    @Test
    void testCurrencySQLException() {
        CurrencyError exception = assertThrows(CurrencyError.class, () -> {
            throw new CurrencyError ( "Error retrieving currencies from database" );
        });
        assertEquals("Error retrieving currencies from database", exception.getMessage(), "should be equal");
    }


    @AfterEach
    void tearDown() {
        subject = null;
    }
}
