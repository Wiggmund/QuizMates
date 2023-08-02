package org.example.quizMates.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionDriverManager implements DBConnection {
    private final DBConfig dbConfig;

    private DBConnectionDriverManager() {
        this.dbConfig = PostgreSQLConfig.getInstance();
    }

    private static class DBConnectionDriverManagerSingleton {
        private static final DBConnection INSTANCE = new DBConnectionDriverManager();
    }

    public static DBConnection getInstance() {
        return DBConnectionDriverManagerSingleton.INSTANCE;
    }

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
