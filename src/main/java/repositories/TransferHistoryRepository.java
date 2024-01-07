package repositories;

import config.DatabaseConnection;
import entitries.Account;
import entitries.TransferHistory;
import exceptions.TransactionError;
import exceptions.TransferHistoryError;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TransferHistoryRepository implements CrudOperations<TransferHistory, Long>{

    private final AccountRepository accountRepository;

    @Override
    public List<TransferHistory> findAll(){
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<TransferHistory> transferHistories = new ArrayList<> ();
        final String query = "SELECT * FROM \"transfer_history\"";
        try  {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                long debitTransactionId = rs.getLong("debit_transaction_id");
                long creditTransactionId = rs.getLong("credit_transaction_id");
                Account debitTransaction = (debitTransactionId != 0L) ? this.accountRepository.findById(debitTransactionId) : null;
                Account creditTransaction = (creditTransactionId != 0L) ? this.accountRepository.findById(creditTransactionId) : null;
                TransferHistory transferHistory = TransferHistory.builder()
                        .id ( rs.getLong ( "id" ) )
                        .transferDate ( rs.getTimestamp ( "transfer_date" ).toLocalDateTime ( ) )
                        .debitTransaction ( debitTransaction )
                        .creditTransaction ( creditTransaction )
                        .build();
                transferHistories.add(transferHistory);
            }

            return transferHistories;
        } catch ( SQLException e) {
            e.printStackTrace ();
            throw new TransferHistoryError ("Error retrieving accounts from database");
        } finally {
            blockFinnally ( connection, stmt, rs );
        }
    }

    @Override
    public List<TransferHistory> saveAll(List<TransferHistory> toSaves){
        List<TransferHistory> savedTransferHistories = new ArrayList<>();
        for (TransferHistory transferHistory : toSaves) {
            TransferHistory savedTransferHistory = this.save (transferHistory);
            savedTransferHistories.add(savedTransferHistory);
        }
        return savedTransferHistories;
    }

    @Override
    public List<TransferHistory> updateAll(List<TransferHistory> toUpdates){
        List<TransferHistory> updatedTransferHistories = new ArrayList<>();
        for (TransferHistory transferHistory : toUpdates) {
            TransferHistory updatedTransferHistory = this.update(transferHistory);
            updatedTransferHistories.add(updatedTransferHistory);
        }
        return updatedTransferHistories;
    }

    @Override
    public TransferHistory save(TransferHistory toSave){
        final String query = "INSERT INTO \"transfer_history\" ( debit_transaction_id , credit_transaction_id , transfer_date) VALUES ( ?, ?, ?)";
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            this.setTransferHistory( toSave, stmt );
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try  {
                    ResultSet keys = stmt.getGeneratedKeys();
                    if (keys.next()) {
                        Long generatedId = keys.getLong(1);
                        toSave.setId(generatedId);
                        return toSave;
                    }
                    throw new TransferHistoryError ("Error saving transfer history");
                } catch ( SQLException e ){
                    e.printStackTrace ();
                    throw new TransferHistoryError ("Error saving transaction");
                }
            }
            throw new TransferHistoryError ("Error saving transfer history");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new TransferHistoryError ("Error saving transfer history");
        }finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new TransferHistoryError ("Error closing database related resources ");
            }
        }
    }

    @Override
    public TransferHistory update(TransferHistory toUpdate){
        final TransferHistory transferHistory = this.findById(toUpdate.getId());
        if (toUpdate.getId() != null && transferHistory != null) {
            Connection connection = null;
            PreparedStatement stmt = null;
            final String query = "UPDATE \"transfer_history\" SET  debit_transaction_id = ? , credit_transaction_id = ? , transfer_date = ? WHERE id = ?";
            try {
                connection = DatabaseConnection.getConnection();
                stmt = connection.prepareStatement(query);
                this.setTransferHistory ( toUpdate, stmt );
                stmt.setLong(4, toUpdate.getId());
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    return toUpdate;
                }
                throw new TransferHistoryError ("Error modifying transfer history");

            } catch (SQLException e) {
                e.printStackTrace ();
                throw new TransferHistoryError ("Error modifying transfer history");
            } finally {
                try {
                    if (stmt != null) stmt.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace ();
                    throw new TransferHistoryError ("Error closing database related resources ");
                }
            }
        }
        throw new TransferHistoryError ("Error modifying transfer history");
    }

    @Override
    public TransferHistory findById(Long id){
        final String query = "SELECT * FROM \"transfer_history\" WHERE id = ? ";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try  {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setLong ( 1, id );
            rs = stmt.executeQuery();
            if (rs.next()) {
                long debitTransactionId = rs.getLong("debit_transaction_id");
                long creditTransactionId = rs.getLong("credit_transaction_id");
                Account debitTransaction = (debitTransactionId != 0L) ? this.accountRepository.findById(debitTransactionId) : null;
                Account creditTransaction = (creditTransactionId != 0L) ? this.accountRepository.findById(creditTransactionId) : null;
                return TransferHistory.builder()
                        .id ( rs.getLong ( "id" ) )
                        .transferDate ( rs.getTimestamp ( "transfer_date" ).toLocalDateTime ( ) )
                        .debitTransaction ( debitTransaction )
                        .creditTransaction ( creditTransaction )
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
    public TransferHistory deleteById(Long id){
        Connection connection = null;
        PreparedStatement stmt = null;
        final String query = "DELETE FROM \"transfer_history\" WHERE id = ?";
        TransferHistory transferHistory = this.findById(id);
        try {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setLong(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                return transferHistory;
            }
            throw new TransferHistoryError ("transfer history not find");
        } catch (SQLException e) {
            e.printStackTrace ();
            throw new TransferHistoryError ("transfer history not find");
        }finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace ();
                throw new TransferHistoryError ("Error closing database related resources ");
            }
        }
    }

    private void setTransferHistory(TransferHistory toSave, PreparedStatement stmt) throws SQLException {
        Account creditTransaction = toSave.getCreditTransaction ();
        Account debitTransaction = toSave.getDebitTransaction ();
        if (debitTransaction.getId () != null && creditTransaction.getId () != null){
            Account debitUpdated = this.accountRepository.update ( debitTransaction );
            Account creditUpdated = this.accountRepository.update ( creditTransaction );
            stmt.setLong ( 1,  debitUpdated.getId ());
            stmt.setLong ( 2,  creditUpdated.getId ());
        } else {
            Account accountSaved = this.accountRepository.save ( debitTransaction );
            Account accountSavedCredit = this.accountRepository.save ( creditTransaction );
            stmt.setLong ( 1,  accountSaved.getId ());
            stmt.setLong ( 2,  accountSavedCredit.getId ());
        }
        stmt.setTimestamp ( 3, Timestamp.valueOf ( toSave.getTransferDate () ) );
    }


    private void blockFinnally(Connection connection, PreparedStatement stmt, ResultSet rs){
        createStmt ( connection, stmt, rs );
    }

    static void createStmt(Connection connection, PreparedStatement stmt, ResultSet rs){
        TransactionRepository.finnally ( connection, stmt, rs );
    }


}
