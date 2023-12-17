package com.fonctionality.repository;

import com.fonctionality.config.DatabaseConnection;
import com.fonctionality.entity.Currency;
import com.fonctionality.entity.CurrencyValue;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyValueRepository implements CrudOperations<CurrencyValue, Long>{

    @Override
    public List<CurrencyValue> findAll(){
        List<CurrencyValue> currencyValues = new ArrayList<> ();
        final String selectQuery = "SELECT * FROM \"currencyValue\"";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement ( selectQuery)
        ){
            ResultSet resultSet = preparedStatement.executeQuery ( );
            while (resultSet.next ()){
                CurrencyValue currencyValue = CurrencyValue.builder ()
                        .id ( resultSet.getLong ( "id" ) )
                        .sourceCurrency( Currency.builder().id(resultSet.getLong("source_currency")).build())
                        .destinationCurrency( Currency.builder().id(resultSet.getLong("destination_currency")).build())
                        .amount ( resultSet.getDouble ( "amount") )
                        .effectiveDate ( resultSet.getTimestamp ( "effective_date" ).toLocalDateTime () )
                        .build ();
                currencyValues.add ( currencyValue );
            }
            return currencyValues;
        } catch ( SQLException e ) {
            throw new RuntimeException ( e );
        }
    }

    @Override
    public List<CurrencyValue> saveAll(List<CurrencyValue> toSaves){
        List<CurrencyValue> saveCurrencyValues = new ArrayList<> ();
        for (CurrencyValue currencyValue : toSaves){
            CurrencyValue updatedCurrencyValue = save ( currencyValue );
            saveCurrencyValues.add ( updatedCurrencyValue );
        }
        return saveCurrencyValues;
    }

    @Override
    public List<CurrencyValue> updateAll(List<CurrencyValue> toUpdates){
        List<CurrencyValue> updatedCurrencyValues = new ArrayList<> ();
        for (CurrencyValue currencyValue : toUpdates){
            CurrencyValue updatedCurrencyValue = update ( currencyValue );
            updatedCurrencyValues.add ( updatedCurrencyValue );
        }
        return updatedCurrencyValues;
    }

    @Override
    public CurrencyValue save(CurrencyValue toSave){
        final  String query = "INSERT INTO \"currencyValue\" (sourceCurrency, destinationCurrency, amount, effectiveDate) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection ();
            PreparedStatement pstmt = connection.prepareStatement ( query )
        ){
            pstmt.setLong ( 1, toSave.getSourceCurrency ().getId ( ) );
            pstmt.setLong ( 2, toSave.getDestinationCurrency ().getId () );
            pstmt.setDouble ( 3, toSave.getAmount () );
            pstmt.setTimestamp ( 4, Timestamp.valueOf ( toSave.getEffectiveDate () ) );
            int rows = pstmt.executeUpdate ( );
            if (rows > 0){
                try (ResultSet keys = pstmt.getGeneratedKeys ( )){
                    if (keys.next ()){
                        Long generatedId = keys.getLong ( 1);
                        toSave.setId ( generatedId );
                        return toSave;
                    }
                    throw new RuntimeException ("Failed to retrieve generated id for currency");
                }
            }
            throw new RuntimeException ( "CurrencyValue creation failed" );
        } catch ( SQLException e ) {
            throw new RuntimeException ( "Error saving currency", e );
        }
    }

    @Override
    public CurrencyValue update(CurrencyValue toUpdate){
        try (Connection connection = DatabaseConnection.getConnection ()){
            final String updateCurrencyValue = "UPDATE \"currencyValue\" SET sourceCurrency = ?, destinationCurrency = ?, amount = ?, effectiveDate = ?";
            try (PreparedStatement pstmtCurrencyValue = connection.prepareStatement ( updateCurrencyValue)){
                if (toUpdate.getSourceCurrency () != null){
                    updateCurrencyValue(toUpdate, pstmtCurrencyValue);
                    pstmtCurrencyValue.setLong ( 2, toUpdate.getId () );
                    int rows = pstmtCurrencyValue.executeUpdate ( );
                    if (rows > 0){
                        return toUpdate;
                    }else {
                        throw new RuntimeException ("Account update failed. Account not found with id: " + toUpdate.getId ());
                    }
                }
            }
        } catch ( SQLException e ) {
            throw new RuntimeException ("Error updating currencyValue", e );
        }
        return toUpdate;
    }


    @Override
    public CurrencyValue findById(Long id){
        try (Connection connection = DatabaseConnection.getConnection ()){
            final String query = "SELECT * FROM \"currencyValue\" WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement ( query);
            pstmt.setLong ( 1, id );
            ResultSet resultSet = pstmt.executeQuery ( );
            if (resultSet.next ()){
                return CurrencyValue.builder ()
                        .id ( resultSet.getLong ( "id") )
                        .sourceCurrency( Currency.builder().id(resultSet.getLong("source_currency")).build())
                        .destinationCurrency( Currency.builder().id(resultSet.getLong("destination_currency")).build())
                        .amount ( resultSet.getDouble ( "amount") )
                        .effectiveDate ( resultSet.getTimestamp ( "effective_date" ).toLocalDateTime () )
                        .build ();
            }else {
                throw new RuntimeException ("CurrencyValue not find");
            }
        } catch ( SQLException e ) {
            throw new RuntimeException ( e );
        }
    }

    @Override
    public CurrencyValue deleteById(Long id){
        CurrencyValue currencyValue = findById ( id );
        try (Connection connection = DatabaseConnection.getConnection ()){
            final String query = "DELETE FROM \"currencyValue\" WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement ( query);
            pstmt.setLong ( 1, id );
            int rows = pstmt.executeUpdate ( );
            if (rows > 0){
                return currencyValue;
            }
            throw new RuntimeException ("CurrencyValue not find");

        } catch ( SQLException e ) {
            throw new RuntimeException ( e );
        }

    }

    private void updateCurrencyValue(CurrencyValue toUpdate, PreparedStatement pstmtCurrencyValue) throws SQLException{
        pstmtCurrencyValue.setLong ( 1, toUpdate.getSourceCurrency ().getId ( ) );
        pstmtCurrencyValue.setLong ( 2, toUpdate.getDestinationCurrency ().getId () );
        pstmtCurrencyValue.setDouble ( 3, toUpdate.getAmount () );
        pstmtCurrencyValue.setTimestamp ( 4, Timestamp.valueOf ( toUpdate.getEffectiveDate () ) );
    }
}
