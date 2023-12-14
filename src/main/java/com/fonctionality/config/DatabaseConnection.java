package com.fonctionality.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection(){
        try {
            return DriverManager.getConnection (
                    System.getenv ( "PG_URL" ),
                    System.getenv ( "PG_USER" ),
                    System.getenv ( "PG_PASSWORD" )
            );
        } catch ( SQLException e ) {
            throw new RuntimeException ( e );
        }
    }
}
