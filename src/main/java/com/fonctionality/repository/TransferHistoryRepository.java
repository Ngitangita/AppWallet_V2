package com.fonctionality.repository;

import com.fonctionality.config.DatabaseConnection;
import com.fonctionality.entity.TransferHistory;

import java.sql.*;
import java.util.List;

public class TransferHistoryRepository implements CrudOperations<TransferHistory, Long>{
    @Override
    public List<TransferHistory> findAll() {
        return null;
    }

    @Override
    public List<TransferHistory> saveAll(List<TransferHistory> toSaves) {
        return null;
    }

    @Override
    public List<TransferHistory> updateAll(List<TransferHistory> toUpdates) {
        return null;
    }

    @Override
    public TransferHistory save(TransferHistory toSave) {
        final String query = "INSERT INTO \"transfer_history\" (debittransaction_id, credittransaction_id, transferdate) VALUES (?, ?, ?)";
        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setLong(1, toSave.getDebitTransaction().getId());
            pstmt.setLong(2, toSave.getCreditTransaction().getId());
            pstmt.setTimestamp(3, Timestamp.valueOf(toSave.getTransferDate()));
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        Long generatedId = keys.getLong(1);
                        toSave.setId(generatedId);
                        return toSave;
                    }
                    throw new RuntimeException("Failed to retrieve generated ID for transfer history");
                }
            }
            throw new RuntimeException("Transfer history creation failed");
        } catch (SQLException e) {
            throw new RuntimeException("Error saving Transfer history", e);
        }
    }

    @Override
    public TransferHistory update(TransferHistory toUpdate) {
        return null;
    }

    @Override
    public TransferHistory findById(Long id) {
        return null;
    }

    @Override
    public TransferHistory deleteById(Long id) {
        return null;
    }
}
