package repositories;

import config.DatabaseConnection;
import entitries.Account;
import entitries.TransferHistory;
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
        return null;
    }

    @Override
    public List<TransferHistory> updateAll(List<TransferHistory> toUpdates){
        return null;
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
        return null;
    }

    @Override
    public TransferHistory findById(Long id){
        return null;
    }

    @Override
    public TransferHistory deleteById(Long id){
        return null;
    }

    private void setTransferHistory(TransferHistory toSave, PreparedStatement stmt) throws SQLException {
        Account creditTransaction = toSave.getCreditTransaction ();
        Account debitTransaction = toSave.getDebitTransaction ();
        if (debitTransaction.getId () != null && creditTransaction.getId () != null){
            stmt.setLong ( 1,  debitTransaction.getId ());
            stmt.setLong ( 2,  creditTransaction.getId ());
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
