package com.pluralsight.capstoneV2.data.mysql;

import com.pluralsight.capstoneV2.data.TransactionDao;
import com.pluralsight.capstoneV2.models.Transaction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlTransactionDao extends MySqlDao implements TransactionDao {

    public MySqlTransactionDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Transaction> getAllTransaction() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT transaction_id, customer_name, date, vendor, description, amount FROM transactions ";
        try(Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet row = ps.executeQuery();

            while (row.next()){
                Transaction transaction = mapRow(row);
                transactions.add(transaction);
            }

        }catch (SQLException e){
            System.err.println("Unexpected Error: " + e.getLocalizedMessage());
        }
        return transactions;
    }

    @Override
    public Transaction getById(int transactionId) {
        String sql = "SELECT transaction_id, customer_name, date, vendor, description, amount From transactions WHERE transaction_id = ?";
        try(Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, transactionId);

            ResultSet row = ps.executeQuery();

            if (row.next()){
                return mapRow(row);
            }
        }catch (SQLException e){
            System.err.println("Error in getById for transactionId " + transactionId + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Transaction create(Transaction transaction) {
        String sql = "INSERT INTO transactions (customer_name, date, vendor, description, amount) VALUES (?, ?, ?, ?, ?) ";
        try(Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, transaction.getName());
            if (transaction.getDate() != null){
                preparedStatement.setTimestamp(2, Timestamp.valueOf(transaction.getDate()));
            }else {
                preparedStatement.setNull(2, Types.TIMESTAMP);
            }
            preparedStatement.setString(3, transaction.getVendor());
            preparedStatement.setString(4, transaction.getDescription());
            preparedStatement.setBigDecimal(5, transaction.getAmount());

            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected > 0){
                ResultSet row = preparedStatement.getGeneratedKeys();
                if (row.next()){
                    int newTransactionId = row.getInt(1);
                    return getById(newTransactionId);
                }
            }
            throw new RuntimeException("Transaction creation failed, no ID obtained.");
        }catch (SQLException e){
            System.err.println("Unexpected Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void updateTransaction(int transactionId, Transaction transaction) {
        String sql = "UPDATE transactions SET customer_name = ?, date = ?, vendor = ?, description = ?, amount = ? WHERE transaction_id = ? ";
        try(Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, transaction.getName());
            if (transaction.getDate() != null){
                preparedStatement.setTimestamp(2, Timestamp.valueOf(transaction.getDate()));
            }else {
                preparedStatement.setNull(2, Types.TIMESTAMP);
            }
            preparedStatement.setString(3, transaction.getVendor());
            preparedStatement.setString(4, transaction.getDescription());
            preparedStatement.setBigDecimal(5, transaction.getAmount());
            preparedStatement.setInt(6, transactionId);

            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected == 0){
                System.out.println("No Transactions Found with id: " + transactionId);
            }
        }catch (SQLException e){
            System.err.println("Unexpected Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int transactionId) {
        String sql = "DELETE FROM transactions WHERE transaction_id = ? ";
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, transactionId); // Using the ID from the path

            int rowAffected = statement.executeUpdate();
            if (rowAffected == 0){
                System.out.println("No category of type " + transactionId + " found");
            }
        }catch (SQLException e){
            System.err.println("Error in update transactionId " + transactionId + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Transaction mapRow(ResultSet row) throws SQLException{
        int transaction_id = row.getInt("transaction_id");
        String name = row.getString("customer_name");
        Timestamp timestamp = row.getTimestamp("date");
        LocalDateTime originalDateTime = null;
        if (timestamp != null){
            originalDateTime = timestamp.toLocalDateTime();
        }
//        String formattedDateTimeString = null;
//        if (originalDateTime != null){
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            formattedDateTimeString = originalDateTime.format(formatter);
//        }
        String vendor = row.getString("vendor");
        String description = row.getString("description");
        BigDecimal amount = row.getBigDecimal("amount");

        return  new Transaction(transaction_id, name, originalDateTime, vendor, description, amount);
    }
}
