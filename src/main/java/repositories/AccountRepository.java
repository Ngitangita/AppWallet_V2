package repositories;

import config.DatabaseConnection;
import entitries.*;
import exceptions.AccountError;
import exceptions.CurrencyError;


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
    public List<Account> findAll(){
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Account> accounts = new ArrayList<>();
        final String query = "SELECT * FROM \"account\"";
        try  {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                long currencyId = rs.getLong("currency_id");
                Currency currency = (currencyId != 0L) ? this.currencyRepository.findById(currencyId) : null;
                Account account = Account.builder()
                        .id(rs.getLong("id"))
                        .name( AccountName.valueOf(rs.getString("name")))
                        .balance(rs.getDouble("balance"))
                        .currency(currency)
                        .lastUpdateDateTime(rs.getTimestamp("last_update_date_time").toLocalDateTime())
                        .account_type( TypeAccount.valueOf(rs.getString("account_type")))
                        .build();
                accounts.add(account);
            }

            return accounts;
        } catch (SQLException e) {
            e.printStackTrace ();
            throw new AccountError ("Error retrieving accounts from database");
        } finally {
            blockFinnally ( connection, stmt, rs );
        }
    }


    @Override
    public List<Account> saveAll(List<Account> toSaves) {
        List<Account> savedAccounts = new ArrayList<>();
        for (Account account : toSaves) {
            Account updatedAccount = this.save(account);
            savedAccounts.add(updatedAccount);
        }
        return savedAccounts;
    }


    @Override
    public List<Account> updateAll(List<Account> toUpdates) {
        List<Account> updatedAccounts = new ArrayList<>();
        for (Account account : toUpdates) {
            Account updatedAccount = this.update(account);
            updatedAccounts.add(updatedAccount);
        }
        return updatedAccounts;
    }


    @Override
    public Account save(Account toSave) {
        Connection connection = null;
        PreparedStatement stmt = null;
        final String query = "INSERT INTO \"account\" (name, balance, last_update_date_time, currency_id, account_type) VALUES (?, ?, ?, ?, ?)";
        try {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            this.setAccount ( toSave, stmt );
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try  {
                    ResultSet keys = stmt.getGeneratedKeys();
                    if (keys.next()) {
                        Long generatedId = keys.getLong(1);
                        toSave.setId(generatedId);
                        return toSave;
                    }else {
                        throw new AccountError ("Error saving account");
                    }
                } catch ( SQLException e ){
                    e.printStackTrace ();
                    throw new AccountError ("Error saving account");
                }
            }else {
                throw new AccountError ("Error saving account");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountError ("Error saving account");
        }finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new AccountError ("Error closing database related resources ");
            }
        }
    }


    @Override
    public Account update(Account toUpdate) {
        final Account account = this.findById(toUpdate.getId());
        if (toUpdate.getId() != null && account != null) {
            Connection connection = null;
            PreparedStatement stmt = null;
            String query = "UPDATE \"account\" SET name = ?, balance = ?, last_update_date_time = ?, currency_id = ?, account_type = ? WHERE id = ?";
            try {
                connection = DatabaseConnection.getConnection();
                stmt = connection.prepareStatement(query);
                this.saveOrUpdateCurrency ( toUpdate );
                this.setAccount ( toUpdate, stmt );
                stmt.setLong(6, toUpdate.getId());
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    return toUpdate;
                }
                throw new AccountError ("Error modifying account");

            } catch (SQLException e) {
                e.printStackTrace ();
                throw new AccountError ("Error modifying account");
            } finally {
                try {
                    if (stmt != null) stmt.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace ();
                    throw new AccountError ("Error closing database related resources ");
                }
            }
        }
        throw new CurrencyError ("Error modifying currency");
    }


    @Override
    public Account findById(Long id) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        final String query = "SELECT * FROM \"account\" WHERE id = ?";
        try  {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setLong ( 1, id );
            rs = stmt.executeQuery();
            if (rs.next()) {
                long currencyId = rs.getLong("currency_id");
                Currency currency = (currencyId != 0L) ? this.currencyRepository.findById(currencyId) : null;
                return Account.builder()
                        .id(rs.getLong("id"))
                        .name( AccountName.valueOf(rs.getString("name")))
                        .balance(rs.getDouble("balance"))
                        .currency(currency)
                        .lastUpdateDateTime(rs.getTimestamp("last_update_date_time").toLocalDateTime())
                        .account_type( TypeAccount.valueOf(rs.getString("account_type")))
                        .build();

            }
            throw new AccountError ("Error retrieving account from database");
        } catch (SQLException e) {
            e.printStackTrace ();
            throw new AccountError ("Error retrieving account from database");
        } finally {
            blockFinnally ( connection, stmt, rs );
        }
    }


    @Override
    public Account deleteById(Long id) {
        Connection connection = null;
        PreparedStatement stmt = null;
        final String query = "DELETE FROM \"account\" WHERE id = ?";
        Account account = this.findById(id);
        try {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setLong(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                return account;
            }
            throw new AccountError ("Currency not find");
        } catch (SQLException e) {
            e.printStackTrace ();
            throw new AccountError ("Currency not find");
        }finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace ();
                throw new AccountError ("Error closing database related resources ");
            }
        }
    }


    private void saveOrUpdateCurrency(Account toUpdate){
        Currency currency = toUpdate.getCurrency ();
        if (currency != null){
            if (currency.getId () != null){
                currency = this.currencyRepository.update ( currency );
            }else{
                currency = this.currencyRepository.save ( currency );
            }
            toUpdate.setCurrency ( currency );
        }
    }


    private void setAccount(Account toSave, PreparedStatement stmt) throws SQLException {
        Currency currency = toSave.getCurrency();
        String accountName = String.valueOf(toSave.getName());
        Double balance = toSave.getBalance();
        LocalDateTime lastUpdateDateTime = toSave.getLastUpdateDateTime();
        TypeAccount accountType = toSave.getAccount_type();
        stmt.setString(1, accountName);
        stmt.setDouble(2, balance != null ? balance : 0 );
        stmt.setTimestamp(3, Timestamp.valueOf(lastUpdateDateTime) );

        if (currency != null) {
            this.saveOrUpdateCurrency(toSave);
            stmt.setLong(4, currency.getId());
            stmt.setString(5, String.valueOf ( accountType ) );
        } else {
            stmt.setNull(4, Types.BIGINT);
            stmt.setString(5, String.valueOf ( accountType ) );
        }
    }


    private void blockFinnally(Connection connection, PreparedStatement stmt, ResultSet rs){
        createStmt ( connection, stmt, rs );
    }

    static void createStmt(Connection connection, PreparedStatement stmt, ResultSet rs){
        TransactionRepository.finnally ( connection, stmt, rs );
    }

}
