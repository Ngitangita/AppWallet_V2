package com.wallet.appwalletapi.repositories;

import com.wallet.appwalletapi.entitries.Category;
import com.wallet.appwalletapi.entitries.TypeCategory;
import com.wallet.appwalletapi.exceptions.CategoryError;
import com.wallet.appwalletapi.repositories.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryTest {

    private CategoryRepository subject = null;


    @BeforeEach
    void setUp() {
        subject = new CategoryRepository (  );
    }


    @Test
    void testFindAllCategorySuccess() {
        assertDoesNotThrow ( () -> {
            int size = 2;
            List<Category> categories = subject.findAll();
            assertNotNull(categories, "List of categories should not be null");
            assertFalse(categories.isEmpty(), "List of categories should not be empty");
            assertEquals(size, categories.size(),  "List should contain two categories");
        } );
    }


    @Test
    void testFindByIdCategorySuccess() {
        assertDoesNotThrow ( () -> {
            Long id = 1L;
            Category category = subject.findById (id);
            List<Category> categories = subject.findAll();
            assertNotNull(category.getId (), "category id should not be null");
            assertTrue (categories.contains ( category ), "List of category should contain currency");
        } );
    }


    @Test
    void testSaveCategorySuccess() {
        assertDoesNotThrow ( () -> {
            Category category =  Category.builder ( )
                    .name("Pomme")
                    .categoryType ( TypeCategory.FOOD )
                    .build ();
            Category categorySaved = subject.save ( category );
            List<Category> categories = subject.findAll();
            assertNotNull(categorySaved.getId (), "currency id should not be null");
            assertTrue (categories.contains ( categorySaved ), "List of category should contain categorySaved");
            assertEquals(category.getName (), categorySaved.getName (),  "the name of currency and the name of currencySave should be equals");
            assertEquals(category.getCategoryType (), categorySaved.getCategoryType (),  "the type of category and the type of categorySave should be equals");
        } );
    }


    @Test
    void testUpdateByIdCategorySuccess() {
        assertDoesNotThrow ( () -> {
            Long id = 4L;
            Category categoryBeforeUpdated = subject.findById ( id );
            Category category = subject.findById ( id );
            category.setName ( "Update Name" );
            category.setCategoryType ( TypeCategory.FOOD );
            Category categoryAfterUpdated = subject.update ( category );
            List<Category> currencies = subject.findAll();

            assertEquals (category.getId (),categoryAfterUpdated.getId () , "the two id should  be equals");
            assertTrue (currencies.contains ( categoryAfterUpdated ), "List of category should contain categoryAfterUpdated");
            assertNotEquals (categoryBeforeUpdated, categoryAfterUpdated,  "the two category  should be not equals");
        } );
    }


    @Test
    void testDeleteByIdCategorySuccess() {
        assertDoesNotThrow ( () -> {
            Long id = 4L;
            Category categoryBeforeDelete = subject.findById ( id );
            List<Category> categoriesBeforeDeleted = subject.findAll();
            Category categoryDeleted = subject.deleteById ( id );
            List<Category> categoriesAfterDeleted = subject.findAll();

            assertEquals (categoryBeforeDelete.getId (),categoryDeleted.getId () , "the two id should  be equals");
            assertNotEquals (categoriesBeforeDeleted.size () , categoriesAfterDeleted.size (),  "the size of two list  should be not equals");
        } );
    }


    @Test
    void testCategorySQLException() {
        CategoryError exception = assertThrows(CategoryError.class, () -> {
            throw new CategoryError ( "Error retrieving categories from database" );
        });
        assertEquals("Error retrieving categories from database", exception.getMessage(), "should be equal");
    }

    @AfterEach
    void tearDown() {
        subject = null;
    }
}
