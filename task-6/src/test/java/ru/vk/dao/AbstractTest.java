package ru.vk.dao;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import ru.vk.DatabaseCredentials;
import ru.vk.FlywayInitializer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractTest {

    private @NotNull Connection connection;

    @BeforeEach
    protected void initDB() {
        FlywayInitializer.initDatabase(DatabaseCredentials.TEST_DATABASE_NAME.getValue());
    }

    public AbstractTest() {
        try {
            this.connection = DriverManager.getConnection(DatabaseCredentials.URL.getValue()+DatabaseCredentials.TEST_DATABASE_NAME.getValue(), DatabaseCredentials.LOGIN.getValue(), DatabaseCredentials.PASSWORD.getValue());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
