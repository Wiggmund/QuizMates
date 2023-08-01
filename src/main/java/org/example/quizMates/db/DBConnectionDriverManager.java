package org.example.quizMates.db;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@RequiredArgsConstructor
public class DBConnectionDriverManager implements DBConnection {
    private final DBConfig dbConfig;

    @Override
    public Connection getConnection() throws SQLException {
        try {
            Class.forName(dbConfig.getDriverClass());
            return DriverManager.getConnection(
                    dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
