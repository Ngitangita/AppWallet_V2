package com.fonctionality.repository;

import com.fonctionality.config.DatabaseConnection;
import com.fonctionality.entity.CodeCurrency;
import com.fonctionality.entity.Currency;
import com.fonctionality.entity.NameCurrency;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRepository implements CrudOperations<Currency, Long>{
    @Override
    public List<Currency> findAll() {
        String query = "SELECT * FROM \"currency\"";
        List<Currency> currencies = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)
        ) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Currency currency = Currency.builder()
                        .id(rs.getLong("id"))
                        .code(CodeCurrency.valueOf(rs.getString("code")))
                        .name(NameCurrency.valueOf(rs.getString("name")))
                        .build();
                currencies.add(currency);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currencies;
    }

    @Override
    public List<Currency> saveAll(List<Currency> toSaves) {
        List<Currency> savedCurrencies = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection()) {
            final String insertQuery = "INSERT INTO \"currency\" (code, name) VALUES (?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                for (Currency toSave : toSaves) {
                    final long id = toSave.getId();
                    if (id == 0) {
                        pstmt.setString(1, String.valueOf(toSave.getCode()));
                        pstmt.setString(2, String.valueOf(toSave.getName()));
                        int rows = pstmt.executeUpdate();
                        if (rows > 0) {
                            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    toSave.setId(generatedKeys.getLong(1));
                                    savedCurrencies.add(toSave);
                                }
                            }
                        } else {
                            throw new RuntimeException("Insert failed for currency: " + toSave);
                        }
                    }

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving currencies", e);
        }
        return savedCurrencies;
    }

    @Override
    public List<Currency> updateAll(List<Currency> toSaves) {
        List<Currency> upCurrencies = new ArrayList<>();
        final String updateQuery = "UPDATE \"currency\" SET code = ?, name = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(updateQuery)
        ) {
            for (Currency toSave : toSaves) {
                pstmt.setString(1, String.valueOf(toSave.getCode()));
                pstmt.setString(2, String.valueOf(toSave.getName()));
                pstmt.setLong(3, toSave.getId());
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    upCurrencies.add(toSave);
                } else {
                    throw new RuntimeException("Update failed for currency with ID " + toSave.getId());
                }
            }
            return upCurrencies;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating currencies", e);
        }
    }


    @Override
    public Currency save(Currency toSave) {
        final String query = "INSERT INTO \"currency\" (code, name) VALUES (?, ?)";
        try (
              Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setString(1, String.valueOf(toSave.getCode()));
            pstmt.setString(2, String.valueOf(toSave.getName()));
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        Long generatedId = keys.getLong(1);
                        toSave.setId(generatedId);
                        return toSave;
                    }
                    throw new RuntimeException("Failed to retrieve generated ID for currency");
                }
            }
            throw new RuntimeException("Currency creation failed");
        } catch (SQLException e) {
            throw new RuntimeException("Error saving currency", e);
        }
    }

    @Override
    public Currency update(Currency toUpdate) {
        if (toUpdate.getId() != null) {
            final Currency currency = findById(toUpdate.getId());
            try (Connection con = DatabaseConnection.getConnection()) {
                final String query = "UPDATE\"currency\" SET code = ?, name = ? WHERE id = ?";
                PreparedStatement pstmt = con.prepareStatement(query);
                pstmt.setString(1, String.valueOf(toUpdate.getCode()));
                pstmt.setString(2, String.valueOf(toUpdate.getName()));
                pstmt.setLong(3, toUpdate.getId());
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    return currency;
                }
                throw new RuntimeException("Error modifying currency");

            } catch (SQLException e) {
                throw new RuntimeException("Error modification currency", e);
            }
        }
        throw new RuntimeException("Id currency is null");
    }

    @Override
    public Currency findById(Long id) {
        try (Connection con = DatabaseConnection.getConnection()) {
            final String query = "SELECT * FROM \"currency\" WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Currency.builder()
                        .id(rs.getLong("id"))
                        .code(CodeCurrency.valueOf(rs.getString("code")))
                        .name(NameCurrency.valueOf(rs.getString("name")))
                        .build();
            }
            throw new RuntimeException("Currency not find");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Currency deleteById(Long id) {
        Currency currency = findById(id);
        try (Connection con = DatabaseConnection.getConnection()) {
            final String query = "DELETE FROM \"currency\" WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setLong(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                return currency;
            }
            throw new RuntimeException("Currency not find");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
