package com.wallet.appwalletapi.config;

import com.wallet.appwalletapi.config.DatabaseConnection;
import com.wallet.appwalletapi.exceptions.ConnectionException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectionDBTest {

    private static Connection connection = null;


    @BeforeAll
    static void beforeAllTest() {
        connection = DatabaseConnection.getConnection();
    }


    @AfterAll
    static void afterAllTest() throws SQLException{
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }


    @Test
    void testConnectionNotNull() {
        assertNotNull(connection, "The connection must not be null.");
    }


    @Test
    void testConnectionIsClosed() throws SQLException {
        assertFalse ( connection.isClosed(), "The connection must not be closed.");
    }


    @Test
    void testValidConnection() {
        assertDoesNotThrow(() -> {
            Connection connection = DatabaseConnection.getConnection();
            assertNotNull(connection, "The connection must not be null.");
            assertFalse(connection.isClosed(), "The connection must not be closed.");
        });
    }


    @Test
    void testConnectionException() {
        ConnectionException exception = assertThrows(ConnectionException.class, () -> {
            throw new ConnectionException("Error connecting to database.");
        });
        assertEquals("Error connecting to database.", exception.getMessage(), "should return true");
    }
}
