package com.learning.dao;

import com.learning.connection.JdbcConnection;
import com.learning.model.AccountType;
import com.learning.model.Customer;
import com.learning.model.GenderType;

import java.sql.*;
import java.util.UUID;

public class CustomerDao {
    public boolean insertCustomer(Customer customer) {
        String sql = "INSERT INTO customer(uuid, full_name, account_number, address, aadhar_number, gender, account_type, balance, phone_number, username, password)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = JdbcConnection.getConnection().prepareStatement(sql);
            preparedStatement.setObject(1, customer.getUuid(), Types.OTHER);
            preparedStatement.setString(2, customer.getFullName());
            preparedStatement.setString(3, customer.getAccountNumber());
            preparedStatement.setString(4, customer.getAddress());
            preparedStatement.setString(5, customer.getAadharNumber());
            preparedStatement.setString(6, customer.getGender().toString());
            preparedStatement.setString(7, customer.getAccountType().toString());
            preparedStatement.setDouble(8, customer.getBalance());
            preparedStatement.setString(9, customer.getPhoneNumber());
            preparedStatement.setString(10, customer.getUsername());
            preparedStatement.setString(11, customer.getPassword());

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE customer SET full_name = ?, account_number = ?, address = ?, aadhar_number = ?, gender = ?, account_type = ?, balance = ?, phone_number = ?, username = ?, password = ?" +
                " WHERE uuid = ?";

        try {
            PreparedStatement preparedStatement = JdbcConnection.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, customer.getFullName());
            preparedStatement.setString(2, customer.getAccountNumber());
            preparedStatement.setString(3, customer.getAddress());
            preparedStatement.setString(4, customer.getAadharNumber());
            preparedStatement.setString(5, customer.getGender().toString());
            preparedStatement.setString(6, customer.getAccountType().toString());
            preparedStatement.setDouble(7, customer.getBalance());
            preparedStatement.setString(8, customer.getPhoneNumber());
            preparedStatement.setString(9, customer.getUsername());
            preparedStatement.setString(10, customer.getPassword());
            preparedStatement.setObject(11, customer.getUuid(), Types.OTHER);

            int rowsEffected = preparedStatement.executeUpdate();
            return rowsEffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM customer";
        try (Statement stmt = JdbcConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            } else {
                throw new RuntimeException("Failed to retrieve count");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred", e);
        }
    }

    public Customer getByUsername(String username) {
        String sql = "SELECT * FROM customer WHERE username =?";
        try {
            PreparedStatement preparedStatement = JdbcConnection.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Customer(
                        resultSet.getObject("uuid", UUID.class),
                        resultSet.getString("username"),
                        resultSet.getString("full_name"),
                        resultSet.getString("account_number"),
                        resultSet.getString("address"),
                        resultSet.getString("aadhar_number"),
                        GenderType.valueOf(resultSet.getString("gender")),
                        AccountType.valueOf(resultSet.getString("account_type")),
                        resultSet.getDouble("balance"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("password")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
