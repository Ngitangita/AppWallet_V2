package com.wallet.appwalletapi.repositories;

import com.wallet.appwalletapi.config.DatabaseConnection;

import com.wallet.appwalletapi.entitries.Category;
import com.wallet.appwalletapi.entitries.TypeCategory;
import com.wallet.appwalletapi.exceptions.CategoryError;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class CategoryRepository implements CrudOperations<Category, Long> {
    @Override
    public List<Category> findAll(){
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT * FROM \"category\"";
        List<Category> categories = new ArrayList<> ();

        try {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Category category = Category.builder()
                        .id ( rs.getLong("id"))
                        .name ( rs.getString("name") )
                        .categoryType ( TypeCategory.valueOf ( rs.getString ( "type_category" ) ) )
                        .build();
                categories.add(category);
            }

            return categories;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CategoryError ("Error retrieving categories from database");
        } finally {
            blockFinnally ( connection, stmt, rs );
        }
    }

    @Override
    public List<Category> saveAll(List<Category> toSaves){
        List<Category> savedCategories = new ArrayList<>();
        for (Category category : toSaves) {
            Category savedCategory = this.save (category);
            savedCategories.add(savedCategory);
        }
        return savedCategories;
    }

    @Override
    public List<Category> updateAll(List<Category> toUpdates){
        List<Category> updatedCategories = new ArrayList<>();
        for (Category account : toUpdates) {
            Category updatedCategory = this.update(account);
            updatedCategories.add(updatedCategory);
        }
        return updatedCategories;
    }

    @Override
    public Category save(Category toSave){
        final String query = "INSERT INTO \"category\" (name, type_category) VALUES ( ?,  ?)";
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            this.setCategory ( toSave, stmt );
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try  {
                    ResultSet keys = stmt.getGeneratedKeys();
                    if (keys.next()) {
                        Long generatedId = keys.getLong(1);
                        toSave.setId(generatedId);
                        return toSave;
                    }
                    throw new CategoryError ("Error saving category");
                } catch ( SQLException e ){
                    e.printStackTrace ();
                    throw new CategoryError ("Error saving category");
                }
            }
            throw new CategoryError ("Error saving category");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new CategoryError ("Error saving category");
        }finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new CategoryError ("Error closing database related resources ");
            }
        }
    }

    @Override
    public Category update(Category toUpdate){
        final Category category = this.findById(toUpdate.getId());
        if (toUpdate.getId() != null && category != null) {
            final String query = "UPDATE\"category\" SET name = ?, type_category = ? WHERE id = ?";
            Connection connection = null;
            PreparedStatement stmt = null;
            try  {
                connection = DatabaseConnection.getConnection();
                stmt = connection.prepareStatement(query);
                this.setCategory ( toUpdate, stmt );
                stmt.setLong(3, toUpdate.getId());
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    return toUpdate;
                }
                throw new CategoryError ("Error modifying category");

            } catch (SQLException e) {
                e.printStackTrace();
                throw new CategoryError ("Error modifying category");
            } finally {
                try {
                    if (stmt != null) stmt.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new CategoryError ("Error closing database related resources ");
                }
            }
        }
        throw new CategoryError ("Error modifying category");
    }

    @Override
    public Category findById(Long id){
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT * FROM \"category\" WHERE id = ?";

        try {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setLong ( 1, id );
            rs = stmt.executeQuery();

            if (rs.next()) {
                return Category.builder()
                        .id (   rs.getLong("id"))
                        .name (  rs.getString("name") )
                        .categoryType ( TypeCategory.valueOf ( rs.getString ( "type_category" ) ) )
                        .build();
            } else {
                throw new CategoryError ("Error retrieving currency from database");
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new CategoryError ("Error retrieving currency from database");
        } finally {
            blockFinnally ( connection, stmt, rs );
        }
    }

    @Override
    public Category deleteById(Long id){
        Connection connection = null;
        PreparedStatement stmt = null;
        final String query = "DELETE FROM \"category\" WHERE id = ?";
        Category category = this.findById(id);
        try  {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setLong(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                return category;
            }
            throw new CategoryError ("category not find");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CategoryError ("category not find");
        }finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new CategoryError ("Error closing database related resources ");
            }
      }
    }
    private void setCategory(Category toSave, PreparedStatement stmt) throws SQLException{
        stmt.setString ( 1, toSave.getName () );
        stmt.setString ( 2, String.valueOf ( toSave.getCategoryType () ) );
    }


    private void blockFinnally(Connection connection, PreparedStatement stmt, ResultSet rs){
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        } catch ( SQLException e) {
            e.printStackTrace ();
            throw new CategoryError ("Error closing database related resources ");
        }
    }

}
