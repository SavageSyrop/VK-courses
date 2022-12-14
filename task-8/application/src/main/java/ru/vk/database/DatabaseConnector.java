package ru.vk.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {

    public static Connection getConnection() {
        Properties properties = new Properties();
        properties.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        properties.setProperty("dataSource.user", DatabaseCredentials.LOGIN.getValue());
        properties.setProperty("dataSource.password", DatabaseCredentials.PASSWORD.getValue());
        properties.setProperty("dataSource.databaseName", DatabaseCredentials.DATABASE_NAME.getValue());
        HikariConfig config = new HikariConfig(properties);
        Connection connection;
        HikariDataSource dataSource = new HikariDataSource(config);
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
