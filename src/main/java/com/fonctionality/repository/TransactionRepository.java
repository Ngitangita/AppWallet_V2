package com.fonctionality.repository;

import com.fonctionality.config.DatabaseConnection;
import com.fonctionality.entity.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionRepository implements CrudOperations<Transaction, Long>{
    private final AccountRepository accountRepository;

    public TransactionRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        final String query = "SELECT * FROM \"transaction\" ";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Account account = this.accountRepository.findById(rs.getLong("account_id"));
                Transaction transaction = Transaction.builder()
                        .id(rs.getLong("id"))
                        .label(rs.getString("label"))
                        .dateTime(rs.getTimestamp("date_time").toLocalDateTime())
                        .amount(rs.getDouble("amount"))
                        .transactionType(TypeTransaction.valueOf(rs.getString("transaction_type").toUpperCase()))
                        .account(account)
                        .build();
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }


    @Override
    public List<Transaction> saveAll(List<Transaction> toSaves) {
        List<Transaction> transactions = new ArrayList<>();
        for (Transaction transaction : toSaves) {
            Transaction savedTransaction = save(transaction);
            transactions.add(savedTransaction);
        }
        return transactions;
    }

    @Override
    public List<Transaction> updateAll(List<Transaction> toUpdates) {
        List<Transaction> transactions = new ArrayList<>();
        for (Transaction transaction : toUpdates) {
           Transaction updatedTransaction = update(transaction);
           transactions.add(updatedTransaction);
        }
        return transactions;
    }

    @Override
    public Transaction save(Transaction toSave) {
        final String addTransaction = "INSERT INTO \"transaction\" (label, amount, date_time, transaction_type, account_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(addTransaction, Statement.RETURN_GENERATED_KEYS)) {

            if (toSave.getAccount() != null) {
                if (isExistInDataBase(
                        toSave.getAccount().getName(),
                        toSave.getAccount().getBalance(),
                        toSave.getAccount().getLastUpdateDateTime(),
                        toSave.getAccount().getCurrency(),
                        toSave.getAccount().getAccount_type()
                )) {
                    Account account = this.accountRepository.save(toSave.getAccount());
                    toSave.setAccount(account);
                    toSave.getAccount().setId(account.getId());
                }

                pstmt.setString(1, toSave.getLabel());
                pstmt.setDouble(2, toSave.getAmount());
                pstmt.setTimestamp(3, Timestamp.valueOf(toSave.getDateTime()));
                pstmt.setString(4, toSave.getTransactionType().name());

                if (toSave.getAccount().getId() != null) {
                    pstmt.setLong(5, toSave.getAccount().getId());
                } else {
                    pstmt.setNull(5, Types.BIGINT);
                }

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    try (ResultSet keys = pstmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            Long generatedId = keys.getLong(1);
                            toSave.setId(generatedId);
                            return toSave;
                        }
                    }
                }
                throw new RuntimeException("Error saving transaction");
            }
            throw new RuntimeException("Error saving transaction: Account is null");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




    @Override
    public Transaction update(Transaction toUpdate) {
        final String updateTransactionWithAccount = "UPDATE \"transaction\" SET label = ?, amount = ?, date_time = ?, transaction_type = ?, account_id = ? WHERE id = ?";
        final String updateTransactionWithoutAccount = "UPDATE \"transaction\" SET label = ?, amount = ?, date_time = ?, transaction_type = ? WHERE id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmtWithAccount = con.prepareStatement(updateTransactionWithAccount);
             PreparedStatement pstmtWithoutAccount = con.prepareStatement(updateTransactionWithoutAccount)
        ) {
            if (toUpdate.getAccount() != null) {
                if (!isExistInDataBase(
                        toUpdate.getAccount().getName(),
                        toUpdate.getAccount().getBalance(),
                        toUpdate.getAccount().getLastUpdateDateTime(),
                        toUpdate.getAccount().getCurrency(),
                        toUpdate.getAccount().getAccount_type()
                )) {
                    Account account = this.accountRepository.update(toUpdate.getAccount());
                    toUpdate.setAccount(account);
                }

                pstmtWithAccount.setString(1, toUpdate.getLabel());
                pstmtWithAccount.setDouble(2, toUpdate.getAmount());
                pstmtWithAccount.setTimestamp(3, Timestamp.valueOf(toUpdate.getDateTime()));
                pstmtWithAccount.setString(4, String.valueOf(toUpdate.getTransactionType()));

                if (toUpdate.getAccount().getId() != null) {
                    pstmtWithAccount.setLong(5, toUpdate.getAccount().getId());
                } else {
                    pstmtWithAccount.setNull(5, Types.BIGINT);
                }

                pstmtWithAccount.setLong(6, toUpdate.getId());
                int rowsWithAccount = pstmtWithAccount.executeUpdate();
                if (rowsWithAccount > 0) {
                    return toUpdate;
                } else {
                    throw new RuntimeException("Transaction update failed. Transaction not found with id: " + toUpdate.getId());
                }
            } else {
                pstmtWithoutAccount.setString(1, toUpdate.getLabel());
                pstmtWithoutAccount.setDouble(2, toUpdate.getAmount());
                pstmtWithoutAccount.setTimestamp(3, Timestamp.valueOf(toUpdate.getDateTime()));
                pstmtWithoutAccount.setString(4, String.valueOf(toUpdate.getTransactionType()));
                pstmtWithoutAccount.setLong(5, toUpdate.getId());

                int rowsWithoutAccount = pstmtWithoutAccount.executeUpdate();
                if (rowsWithoutAccount > 0) {
                    return toUpdate;
                } else {
                    throw new RuntimeException("Transaction update failed. Transaction not found with id: " + toUpdate.getId());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating transaction", e);
        }
    }


    @Override
    public Transaction findById(Long id) {
        try (Connection con = DatabaseConnection.getConnection()) {

            final String query = "SELECT * FROM \"transaction\" WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Account account = this.accountRepository.findById(rs.getLong("account_id"));
                return  Transaction.builder()
                        .id(rs.getLong("id"))
                        .label(rs.getString("label"))
                        .dateTime(rs.getTimestamp("date_time").toLocalDateTime())
                        .amount(rs.getDouble("amount"))
                        .transactionType(TypeTransaction.valueOf(rs.getString("transaction_type").toUpperCase()))
                        .account(account)
                        .build();
            }
            throw new RuntimeException("Currency not find");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transaction deleteById(Long id) {
        Transaction transaction = findById(id);
        try (Connection con = DatabaseConnection.getConnection()) {
            final String query = "DELETE FROM \"transaction\" WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setLong(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                return transaction;
            }
            throw new RuntimeException("transaction not find");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isExistInDataBase(
            AccountName name,
            Double balance,
            LocalDateTime lastUpdateDateTime,
            Currency currency,
            TypeAccount account_type
    ) {
        List<Account> accounts = this.accountRepository.findAll();
        for (Account account : accounts) {
            if (
                account.getName() == name &&
                Objects.equals(account.getBalance(), balance) &&
                account.getLastUpdateDateTime() == lastUpdateDateTime &&
                account.getCurrency() == currency &&
                account.getAccount_type() == account_type
            ) {
                return true;
            }
        }
        return false;
    }



}
