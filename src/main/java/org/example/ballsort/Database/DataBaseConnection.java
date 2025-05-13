package org.example.ballsort.Database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    // gamedb.crkc8ma0uhlz.eu-west-3.rds.amazonaws.com
    // Ar4LHfYcCyiMGNpUI7c4
    // admin
    private static final String URL = "jdbc:mysql://gamedb.crkc8ma0uhlz.eu-west-3.rds.amazonaws.com:3306/jgame_db";
    private static final String USER = "admin";
    private static final String PASSWORD = "Ar4LHfYcCyiMGNpUI7c4";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database at " + URL + ": " + e.getMessage(), e);
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to close database connection: " + e.getMessage(), e);
            }
        }
    }
}
