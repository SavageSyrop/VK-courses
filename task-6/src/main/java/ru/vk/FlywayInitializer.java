package ru.vk;

import org.flywaydb.core.Flyway;

public final class  FlywayInitializer {
    public static void initDatabase(String databaseName) {
        final Flyway flyway = Flyway.configure()
                .dataSource(
                        DatabaseCredentials.URL.getValue() + databaseName,
                        DatabaseCredentials.LOGIN.getValue(),
                        DatabaseCredentials.PASSWORD.getValue()
                )
                .cleanDisabled(false)
                .locations("db")
                .load();
        flyway.clean();
        flyway.migrate();
    }
}