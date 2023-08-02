package org.example.quizMates.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PostgreSQLConfig implements DBConfig {
    private final String url;
    private final String username;
    private final String password;
    private final String driver;

    private PostgreSQLConfig() {
        try(InputStream source = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties props = new Properties();
            props.load(source);

            this.url = props.getProperty("PSQL_URL");
            this.username = props.getProperty("PSQL_USERNAME");
            this.password = props.getProperty("PSQL_PASSWORD");
            this.driver = props.getProperty("PSQL_DRIVER_CLASS");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class PostgreSQlConfigSingleton {
        private static final DBConfig INSTANCE = new PostgreSQLConfig();
    }

    public static DBConfig getInstance() {
        return PostgreSQlConfigSingleton.INSTANCE;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getDriverClass() {
        return driver;
    }
}
