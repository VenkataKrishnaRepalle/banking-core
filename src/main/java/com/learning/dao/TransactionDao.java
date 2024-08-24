package com.learning.dao;

import com.learning.connection.JdbcConnection;
import com.learning.model.Transaction;
import com.learning.model.TransactionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionDao {
    public boolean insert(Transaction transaction) {
        String sql = "INSERT INTO transaction(uuid, customer_uuid, transaction_type, transaction_amount, transaction_time) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = JdbcConnection.getConnection().prepareStatement(sql);
            preparedStatement.setObject(1, transaction.getUuid(), Types.OTHER);
            preparedStatement.setObject(2, transaction.getCustomerUuid(), Types.OTHER);
            preparedStatement.setString(3, transaction.getTransactionType().toString());
            preparedStatement.setDouble(4, transaction.getTransactionAmount());
            preparedStatement.setTimestamp(5, Timestamp.from(transaction.getTransactionDate()));

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Transaction> getAllByCustomerId(UUID uuid) {
        String sql = "SELECT * FROM transaction WHERE customer_uuid =?";
        List<Transaction> transactions = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = JdbcConnection.connection.prepareStatement(sql);
            preparedStatement.setObject(1, uuid, Types.OTHER);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = Transaction.builder()
                        .uuid(UUID.fromString(resultSet.getString("uuid")))
                        .customerUuid(UUID.fromString(resultSet.getString("customer_uuid")))
                        .transactionType(TransactionType.valueOf(resultSet.getString("transaction_type")))
                        .transactionAmount(resultSet.getDouble("transaction_amount"))
                        .transactionDate(resultSet.getTimestamp("transaction_time").toInstant())
                        .build();
                transactions.add(transaction);
            }

            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
