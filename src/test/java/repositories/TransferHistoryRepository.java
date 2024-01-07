package repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class TransferHistoryRepository {
    private TransferHistoryRepository subject = null;


    @BeforeEach
    void setUp() {
        subject = new TransferHistoryRepository (  );
    }


    @AfterEach
    void tearDown() {
        subject = null;
    }
}
