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
        List<Account> savedAccounts = new ArrayList<>();
        for (Account account : toSaves) {
            Account updatedAccount = save(account);
            savedAccounts.add(updatedAccount);
        }
        return savedAccounts;
    }



    @Override
    public List<Account> updateAll(List<Account> toUpdates) {
        List<Account> updatedAccounts = new ArrayList<>();
        for (Account account : toUpdates) {
            Account updatedAccount = update(account);
            updatedAccounts.add(updatedAccount);
        }
        return updatedAccounts;
    }


    @Override
    public Account save(Account toSave) {
        final String addAccount = "INSERT INTO \"account\" (name, balance, last_update_date_time, currency_id, account_type) VALUES (?, ?, ?, ?, ?)";
        final String addAccountNotCurrency = "INSERT INTO \"account\" (name, balance, last_update_date_time, account_type) VALUES (?, ?, ?,  ?)";
        final String addCurrency = "INSERT INTO \"currency\" (code, name) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmtAccount = con.prepareStatement(addAccount, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement pstmtAccountNotCurrency = con.prepareStatement(addAccountNotCurrency, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement pstmtCurrency = con.prepareStatement(addCurrency, Statement.RETURN_GENERATED_KEYS)
        ) {

            if (toSave.getCurrency() != null) {
                Account savedAccount = toAccount(toSave, pstmtAccount, pstmtCurrency);
                int rows = pstmtAccount.executeUpdate();
                if (rows > 0) {
                    try (ResultSet keys = pstmtAccount.getGeneratedKeys()) {
                        if (keys.next()) {
                            Long generatedId = keys.getLong(1);
                            savedAccount.setId(generatedId);
                            return savedAccount;
                        }
                        throw new RuntimeException("Failed to retrieve generated ID for Account");
                    }
                }
            } else {
                pstmtAccountNotCurrency.setString(1, String.valueOf(toSave.getName()));
                pstmtAccountNotCurrency.setDouble(2, toSave.getBalance());
                pstmtAccountNotCurrency.setTimestamp(3, Timestamp.valueOf(toSave.getLastUpdateDateTime()));
                pstmtAccountNotCurrency.setString(4, String.valueOf(toSave.getAccount_type()));
                int rows = pstmtAccountNotCurrency.executeUpdate();
                if (rows > 0) {
                  try ( ResultSet keys = pstmtAccount.getGeneratedKeys()){
                      Long generatedId = keys.getLong(1);
                      toSave.setId(generatedId);
                      return toSave;
                  }catch (SQLException e){
                        throw new RuntimeException(e);
                    }
                }
            }

            throw new RuntimeException("Error saving account");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Account toAccount(Account toSave, PreparedStatement pstmtAccount, PreparedStatement pstmtCurrency) throws SQLException {
        saveCurrencyIfNecessary(pstmtCurrency, toSave.getCurrency());
        updateAccount(toSave, pstmtAccount);
        return toSave;
    }

    @Override
    public Account update(Account toUpdate) {
        try (Connection con = DatabaseConnection.getConnection()) {
            final String updateCurrency = "UPDATE \"currency\" SET code = ?, name = ? WHERE id = ?";
            final String updateAccount = "UPDATE \"account\" SET name = ?, balance = ?, last_update_date_time = ?, currency_id = ?, account_type = ? WHERE id = ?";
            final String updateAccountNotCurrency = "UPDATE \"account\" SET name = ?, balance = ?, last_update_date_time = ?, account_type = ? WHERE id = ?";
            try (PreparedStatement pstmtAccount = con.prepareStatement(updateAccount);
                 PreparedStatement pstmtCurrency = con.prepareStatement(updateCurrency);
                 PreparedStatement pstmtAccountNotCurrency = con.prepareStatement(updateAccountNotCurrency)
            ) {
               if (toUpdate.getCurrency() != null) {
                   updateCurrencyIfNecessary(pstmtCurrency, toUpdate.getCurrency());
                   updateAccount(toUpdate, pstmtAccount);
                   pstmtAccount.setLong(6, toUpdate.getId());
                   int rows = pstmtAccount.executeUpdate();
                   if (rows > 0) {
                       return toUpdate;
                   } else {
                       throw new RuntimeException("Account update failed. Account not found with id: " + toUpdate.getId());
                   }
               } else {
                   pstmtAccountNotCurrency.setString(1, String.valueOf(toUpdate.getName()));
                   pstmtAccountNotCurrency.setDouble(2, toUpdate.getBalance());
                   pstmtAccountNotCurrency.setTimestamp(3, Timestamp.valueOf(toUpdate.getLastUpdateDateTime()));
                   pstmtAccountNotCurrency.setString(4, String.valueOf(toUpdate.getAccount_type()));
                   pstmtAccountNotCurrency.setLong(5, toUpdate.getId());
                   int rows = pstmtAccountNotCurrency.executeUpdate();
                   if (rows > 0) {
                       return toUpdate;
                   } else {
                       throw new RuntimeException("Account update failed. Account not found with id: " + toUpdate.getId());
                   }
               }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating account", e);
        }
    }


    @Override
    public Account findById(Long id) {
        try (Connection con = DatabaseConnection.getConnection()) {
            final String query = "SELECT * FROM \"currency\" WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Currency currency = this.currencyRepository.findById(rs.getLong("currency_id"));
                return Account.builder()
                        .id(rs.getLong("id"))
                        .name(AccountName.valueOf(rs.getString("name")))
                        .balance(rs.getDouble("balance"))
                        .currency(currency)
                        .lastUpdateDateTime((LocalDateTime) rs.getObject("last_update_date_time"))
                        .account_type(TypeAccount.valueOf(rs.getString("account_type")))
                        .build();
            }
            throw new RuntimeException("Currency not find");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account deleteById(Long id) {
        Account account = findById(id);
        try (Connection con = DatabaseConnection.getConnection()) {
            final String query = "DELETE FROM \"account\" WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setLong(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                return account;
            }
            throw new RuntimeException("Account not find");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveCurrencyIfNecessary(PreparedStatement pstmt, Currency currency) throws SQLException {
        if (!isExistInDataBase(currency.getName(), currency.getCode())) {
            toCurrency(pstmt, currency);
        }
    }

    private void updateCurrencyIfNecessary(PreparedStatement pstmt, Currency currency) throws SQLException {
        if (isExistInDataBase(currency.getName(), currency.getCode())) {
            toCurrency(pstmt, currency);
        }
    }

    private void toCurrency(PreparedStatement pstmt, Currency currency) throws SQLException {
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

    private void updateAccount(Account toUpdate, PreparedStatement pstmtAccount) throws SQLException {
        pstmtAccount.setString(1, String.valueOf(toUpdate.getName()));
        pstmtAccount.setDouble(2, toUpdate.getBalance());
        pstmtAccount.setTimestamp(3, Timestamp.valueOf(toUpdate.getLastUpdateDateTime()));
        pstmtAccount.setLong(4, toUpdate.getCurrency().getId());
        pstmtAccount.setString(5, String.valueOf(toUpdate.getAccount_type()));
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
