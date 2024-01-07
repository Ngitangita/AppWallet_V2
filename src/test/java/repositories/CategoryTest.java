package repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class CategoryTest {

    private CategoryRepository subject = null;


    @BeforeEach
    void setUp() {
        subject = new CategoryRepository (  );
    }


    @AfterEach
    void tearDown() {
        subject = null;
    }
}
