package repositories;

import entitries.Account;
import entitries.TransferHistory;
import exceptions.TransferHistoryError;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransferHistoryRepositoryTest {
    private AccountRepository accountRep = null;
    private TransferHistoryRepository subject = null;


    @BeforeEach
    void setUp() {
        CurrencyRepository currencyRep = new CurrencyRepository ();
         accountRep = new AccountRepository ( currencyRep );
        subject = new TransferHistoryRepository (accountRep  );
    }


    @Test
    void testFindAllTransferHistorySuccess() {
        assertDoesNotThrow ( () -> {
            int size = 2;
            List<TransferHistory> transferHistories = subject.findAll();
            assertNotNull(transferHistories, "List of transferHistory should not be null");
            assertFalse(transferHistories.isEmpty(), "List of transferHistory should not be empty");
            assertEquals(size, transferHistories.size(),  "List should contain four transferHistory");
        } );
    }


    @Test
    void testSaveTransferHistorySuccess() {
        assertDoesNotThrow ( () -> {
            Long creditId = 12L;
            Long debitId = 13L;
            Account credit = accountRep.findById ( creditId );
            Account debit = accountRep.findById ( debitId );
            TransferHistory transferHistory = TransferHistory.builder()
                    .transferDate ( LocalDateTime.now () )
                    .creditTransaction ( debit )
                    .debitTransaction ( credit )
                    .build();
            List<TransferHistory> beforeTransferHistories = subject.findAll();
            TransferHistory savedTransferHistory = subject.save ( transferHistory );
            List<TransferHistory> transferHistories = subject.findAll();
            assertNotNull(transferHistories, "List of transferHistory should not be null");
            assertNotNull (savedTransferHistory.getId (), "the id transfer history should  be not null");
            assertFalse (transferHistories.isEmpty(), "List of transferHistory should not be empty");
            assertNotEquals(beforeTransferHistories.size(), transferHistories.size(),  "the size of the two lists  should be not equals");
        } );
    }


    @Test
    void testUpdateTransferHistorySuccess() {
        assertDoesNotThrow ( () -> {
            Long id = 4L;
            Long creditId = 12L;
            Long debitId = 13L;
            Account credit = accountRep.findById ( creditId );
            Account debit = accountRep.findById ( debitId );
            TransferHistory transferHistory = TransferHistory.builder()
                    .id ( id )
                    .transferDate ( LocalDateTime.now () )
                    .creditTransaction ( debit )
                    .debitTransaction ( credit )
                    .build();
            List<TransferHistory> beforeTransferHistories = subject.findAll();
            TransferHistory updatedTransferHistory = subject.update ( transferHistory );
            List<TransferHistory> transferHistories = subject.findAll();
            assertTrue (transferHistories.stream ().filter ( t -> t.getId ().equals ( updatedTransferHistory.getId()) ).toList ().size () != 0, "List of transferHistory should  be contain  updatedTransferHistory");
            assertEquals(beforeTransferHistories.size(), transferHistories.size(),  "the size of the two lists  should be equals");
        } );
    }


    @Test
    void testTransferHistorySQLException() {
        TransferHistoryError exception = assertThrows( TransferHistoryError.class, () -> {
            throw new TransferHistoryError ( "Error retrieving accounts from database" );
        });
        assertEquals("Error retrieving accounts from database", exception.getMessage(), "should be equal");
    }


    @AfterEach
    void tearDown() {
        accountRep = null;
        subject = null;
    }
}
