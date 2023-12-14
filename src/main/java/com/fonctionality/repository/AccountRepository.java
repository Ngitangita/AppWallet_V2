package com.fonctionality.repository;

import com.fonctionality.config.DatabaseConnection;
import com.fonctionality.entity.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository  implements CrudOperations<Account, Long>{
    private final CurrencyRepository currencyRepository;

    public AccountRepository(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        final String selectQuery = "SELECT * FROM \"account\"";
        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement pstmt = con.prepareStatement(selectQuery)
           ){
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Currency currency = this.currencyRepository.findById(rs.getLong("currency_id"));
                Account account = Account.builder()
                        .id(rs.getLong("id"))
                        .name(AccountName.valueOf(rs.getString("name")))
                        .balance(rs.getDouble("balance"))
                        .currency(currency)
                        .lastUpdateDateTime((LocalDateTime) rs.getObject("last_update_date_time"))
                        .account_type(TypeAccount.valueOf(rs.getString("account_type")))
                        .build();
                accounts.add(account);
            }
         return accounts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Account> saveAll(List<Account> toSaves) {
        final String addAccount = "INSERT INTO \"account\" (name, balance, last_update_date_time, currency_id, account_type) VALUES (?, ?, ?, ?, ?)";
        final String addCurrency = "INSERT INTO \"currency\" (code, name) VALUES (?, ?)";
        List<Account> savedAccounts = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmtAccount = con.prepareStatement(addAccount, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement pstmtCurrency = con.prepareStatement(addCurrency, Statement.RETURN_GENERATED_KEYS)
        ) {
                for (Account account : toSaves) {

                    saveCurrencyIfNecessary(pstmtCurrency, account.getCurrency());
                    pstmtAccount.setString(1, String.valueOf(account.getName()));
                    pstmtAccount.setDouble(2, account.getBalance());
                    pstmtAccount.setTimestamp(3, Timestamp.valueOf(account.getLastUpdateDateTime()));
                    pstmtAccount.setLong(4, account.getCurrency().getId());
                    pstmtAccount.setString(5, String.valueOf(account.getAccount_type()));
                    int rows = pstmtAccount.executeUpdate();
                    if (rows > 0) {
                        savedAccounts.add(account);
                    }
                }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving accounts", e);
        }

        return savedAccounts;
    }



    @Override
    public List<Account> updateAll(List<Account> toSaves) {
        return null;
    }

    @Override
    public Account save(Account toSave) {
        return null;
    }

    @Override
    public Account update(Account toUpdate) {
        return null;
    }

    @Override
    public Account findById(Long id) {
        return null;
    }

    @Override
    public Account deleteById(Long id) {
        return null;
    }

    private void saveCurrencyIfNecessary(PreparedStatement pstmt, Currency currency) throws SQLException {
        if (currency != null && currency.getId() == 0 && isExistInDataBase(currency.getName(), currency.getCode())) {
                pstmt.setString(1, String.valueOf(currency.getCode()));
                pstmt.setString(2, String.valueOf(currency.getName()));
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    try (ResultSet keys = pstmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            currency.setId(keys.getLong(1));
                        }
                        throw new RuntimeException("Failed to retrieve generated ID for currency");
                    }
                } else {
                    throw new RuntimeException("Currency creation failed");
                }
        }
    }
    private boolean isExistInDataBase(NameCurrency name, CodeCurrency code) {
        List<Currency> currencies = this.currencyRepository.findAll();
        for (Currency currency : currencies) {
            if (currency.getName() == name && currency.getCode() == code) {
                return true;
            }
        }
        return false;
    }
}
