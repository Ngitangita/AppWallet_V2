package repositories;

import config.DatabaseConnection;
import entitries.Account;
import entitries.Category;
import entitries.Transaction;
import entitries.TypeTransaction;
import exceptions.AccountError;
import exceptions.TransactionError;
import lombok.AllArgsConstructor;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class TransactionRepository implements CrudOperations<Transaction, Long>{
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<Transaction> findAll(){
        final String query = "SELECT * FROM \"transaction\" ";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Transaction> transactions = new ArrayList<>();
        try  {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                long currencyId = rs.getLong("account_id");
                long categoryId = rs.getLong("category_id");
                Account account = (currencyId != 0L) ? this.accountRepository.findById(currencyId) : null;
                Category category = (categoryId != 0L) ? this.categoryRepository.findById(categoryId) : null;
                Transaction transaction = Transaction.builder()
                        .id(rs.getLong("id"))
                        .label(rs.getString("label"))
                        .dateTime(rs.getTimestamp("date_time").toLocalDateTime())
                        .amount(rs.getDouble("amount"))
                        .category ( category )
                        .typeTransaction ( TypeTransaction.valueOf ( rs.getString( "transaction_type") ) )
                        .account(account)
                        .build();
                transactions.add(transaction);
            }

            return transactions;
        } catch (SQLException e) {
            e.printStackTrace ();
            throw new TransactionError ("Error retrieving transaction from database");
        } finally {
            blockFinnally ( connection, stmt, rs );
        }
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
        final String query = "INSERT INTO \"transaction\" (label, amount, date_time, transaction_type, account_id, category_id) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            this.setTransaction ( toSave, stmt );
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try  {
                    ResultSet keys = stmt.getGeneratedKeys();
                    if (keys.next()) {
                        Long generatedId = keys.getLong(1);
                        toSave.setId(generatedId);
                        return toSave;
                    }
                    throw new TransactionError ("Error saving transaction");
                } catch ( SQLException e ){
                    e.printStackTrace ();
                    throw new TransactionError ("Error saving transaction");
                }
            }
            throw new AccountError ("Error saving transaction");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new TransactionError ("Error saving transaction");
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
    public Transaction update(Transaction toUpdate) {
        final Transaction account = this.findById(toUpdate.getId());
        if (toUpdate.getId() != null && account != null) {
            Connection connection = null;
            PreparedStatement stmt = null;
            final String query = "UPDATE \"transaction\" SET label = ?, amount = ?, date_time = ?, transaction_type = ?, account_id = ?, category_id = ? WHERE id = ?";
            try {
                connection = DatabaseConnection.getConnection();
                stmt = connection.prepareStatement(query);
                this.setTransaction ( toUpdate, stmt );
                stmt.setLong(7, toUpdate.getId());
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    return toUpdate;
                }
                throw new TransactionError ("Error modifying transaction");

            } catch (SQLException e) {
                e.printStackTrace ();
                throw new TransactionError ("Error modifying transaction");
            } finally {
                try {
                    if (stmt != null) stmt.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace ();
                    throw new TransactionError ("Error closing database related resources ");
                }
            }
        }
        throw new TransactionError ("Error modifying transaction");
    }


    @Override
    public Transaction findById(Long id) {
        final String query = "SELECT * FROM \"transaction\" WHERE id = ? ";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try  {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setLong ( 1, id );
            rs = stmt.executeQuery();
            if (rs.next()) {
                long currencyId = rs.getLong("account_id");
                long categoryId = rs.getLong("category_id");
                Account account = (currencyId != 0L) ? this.accountRepository.findById(currencyId) : null;
                Category category = (categoryId != 0L) ? this.categoryRepository.findById(categoryId) : null;
                return  Transaction.builder()
                        .id(rs.getLong("id"))
                        .label(rs.getString("label"))
                        .dateTime(rs.getTimestamp("date_time").toLocalDateTime())
                        .amount(rs.getDouble("amount"))
                        .category ( category )
                        .typeTransaction ( TypeTransaction.valueOf ( rs.getString( "transaction_type") ) )
                        .account(account)
                        .build();
            }
            throw new TransactionError ("Error retrieving transaction from database");
        } catch (SQLException e) {
            e.printStackTrace ();
            throw new TransactionError ("Error retrieving transaction from database");
        } finally {
            blockFinnally ( connection, stmt, rs );
        }
    }

    @Override
    public Transaction deleteById(Long id) {
        Connection connection = null;
        PreparedStatement stmt = null;
        final String query = "DELETE FROM \"transaction\" WHERE id = ?";
        Transaction transaction = this.findById(id);
        try {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setLong(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                return transaction;
            }
            throw new TransactionError ("transaction not find");
        } catch (SQLException e) {
            e.printStackTrace ();
            throw new TransactionError ("transaction not find");
        }finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace ();
                throw new TransactionError ("Error closing database related resources ");
            }
        }
    }


    private void setTransaction(Transaction toSave, PreparedStatement stmt) throws SQLException {
        String label = toSave.getLabel ();
        Double amount = toSave.getAmount ();
        LocalDateTime dateTime = toSave.getDateTime ();
        TypeTransaction transactionType = toSave.getTypeTransaction ();
        stmt.setString(1, label);
        stmt.setDouble(2, amount);
        stmt.setTimestamp (3, Timestamp.valueOf (  dateTime  ) );
        stmt.setString (4, String.valueOf ( transactionType ) );
        Account account = toSave.getAccount ();
        Category category = toSave.getCategory ();
        if (account != null){
            if (account.getId () != null){
                account = this.accountRepository.update ( account );
                toSave.setAccount ( account );
            }else{
                account = this.accountRepository.save ( account );
                toSave.setAccount ( account );
            }

        }

        if (category != null){
            if (category.getId () != null){
                category = this.categoryRepository.update ( category );
                toSave.setCategory ( category );
            }else{
                category = this.categoryRepository.save ( category );
                toSave.setCategory ( category );
            }

        }

        if (account != null) {
            stmt.setLong(5, account.getId());
        } else {
            stmt.setNull(5, Types.BIGINT);
        }

        if (category != null) {
            stmt.setLong(6, category.getId());
        } else {
            stmt.setNull(6, Types.BIGINT);
        }
    }



    private void blockFinnally(Connection connection, PreparedStatement stmt, ResultSet rs){
        finnally ( connection, stmt, rs );
    }

    static void finnally(Connection connection, PreparedStatement stmt, ResultSet rs){
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        } catch ( SQLException e) {
            e.printStackTrace ();
            throw new TransactionError ("Error closing database related resources ");
        }
    }

}
