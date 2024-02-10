package com.wallet.appwalletapi.repositories;

import com.wallet.appwalletapi.config.DatabaseConnection;
import com.wallet.appwalletapi.entitries.CodeCurrency;
import com.wallet.appwalletapi.entitries.Currency;
import com.wallet.appwalletapi.entitries.NameCurrency;
import com.wallet.appwalletapi.exceptions.CurrencyError;
import lombok.NoArgsConstructor;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class CurrencyRepository implements CrudOperations<Currency, Long>{
    @Override
    public List<Currency> findAll(){
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT * FROM \"currency\"";
        List<Currency> currencies = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Currency currency = new Currency(
                        rs.getLong("id"),
                        NameCurrency.valueOf(rs.getString("name")),
                        CodeCurrency.valueOf(rs.getString("code"))
                );
                currencies.add(currency);
            }

            return currencies;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CurrencyError ("Error retrieving currencies from database");
        } finally {
            blockFinnally (   connection, stmt, rs );
        }
    }


    @Override
    public List<Currency> saveAll(List<Currency> toSaves) {
        List<Currency> savedCurrencies = new ArrayList<>();
        for (Currency currency : toSaves) {
            Currency savedCurrency = this.save(currency);
            savedCurrencies.add(savedCurrency);
        }
       return savedCurrencies;
    }

    @Override
    public List<Currency> updateAll(List<Currency> toUpdates) {
        List<Currency> upCurrencies = new ArrayList<>();
        for (Currency currency : toUpdates) {
            Currency updatedCurrency = this.update(currency);
            upCurrencies.add(updatedCurrency);
        }

        return upCurrencies;
    }


    @Override
    public Currency save(Currency toSave) {
        final String query = "INSERT INTO \"currency\" (code, name) VALUES (?, ?)";
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, toSave.getCode().toString ());
            stmt.setString(2, toSave.getName().toString ());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        Long generatedId = keys.getLong(1);
                        toSave.setId(generatedId);
                        return toSave;
                    }
                    throw new CurrencyError ("Error saving currency");
                }
            }
            throw new CurrencyError ("Error saving currency");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new CurrencyError ("Error saving currency");
        }finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new CurrencyError("Error closing database related resources ");
            }
        }
    }

    @Override
    public Currency update(Currency toUpdate) {
        final Currency currency = this.findById(toUpdate.getId());
        if (toUpdate.getId() != null && currency != null) {
            final String query = "UPDATE\"currency\" SET code = ?, name = ? WHERE id = ?";
            Connection connection = null;
            PreparedStatement stmt = null;
            try  {
                connection = DatabaseConnection.getConnection();
                stmt = connection.prepareStatement(query);
                stmt.setString(1, toUpdate.getCode().toString ());
                stmt.setString(2, toUpdate.getName().toString ());
                stmt.setLong(3, toUpdate.getId());
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    return toUpdate;
                }
                throw new CurrencyError ("Error modifying currency");

            } catch (SQLException e) {
                e.printStackTrace();
                throw new CurrencyError ("Error modifying currency");
            } finally {
                try {
                    if (stmt != null) stmt.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new CurrencyError("Error closing database related resources ");
                }
            }
        }
        throw new CurrencyError ("Error modifying currency");
    }

    @Override
    public Currency findById(Long id) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT * FROM \"currency\" WHERE id = ?";

        try {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setLong ( 1, id );
            rs = stmt.executeQuery();

            if (rs.next()) {
             return new Currency(
                        rs.getLong("id"),
                        NameCurrency.valueOf(rs.getString("name")),
                        CodeCurrency.valueOf(rs.getString("code"))
                );
            } else {
                throw new CurrencyError("Error retrieving currency from database");
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new CurrencyError("Error retrieving currency from database");
        } finally {
            blockFinnally ( connection, stmt, rs );
        }
    }

    @Override
    public Currency deleteById(Long id) {
        Connection connection = null;
        PreparedStatement stmt = null;
        final String query = "DELETE FROM \"currency\" WHERE id = ?";
        Currency currency = this.findById(id);
        try  {
             connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setLong(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                return currency;
            }
            throw new CurrencyError ("Currency not find");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CurrencyError ("Currency not find");
        }finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new CurrencyError("Error closing database related resources ");
            }
        }
    }
    private void blockFinnally(Connection connection, PreparedStatement stmt, ResultSet rs){
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        } catch ( SQLException e) {
            e.printStackTrace ();
            throw new CurrencyError ("Error closing database related resources ");
        }
    }
}
