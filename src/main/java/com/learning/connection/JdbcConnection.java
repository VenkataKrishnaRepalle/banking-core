package com.learning.connection;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcConnection  {
    public static Connection connection;

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = java.sql.DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/banking-core", "postgres", "root");
            return connection;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
