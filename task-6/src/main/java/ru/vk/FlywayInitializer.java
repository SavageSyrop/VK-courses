package ru.vk;

import org.flywaydb.core.Flyway;

public final class  FlywayInitializer {
    public static void initDatabase() {
        final Flyway flyway = Flyway.configure()
                .dataSource(
                        DatabaseCredentials.URL.getValue() + DatabaseCredentials.DATABASE_NAME.getValue(),
                        DatabaseCredentials.LOGIN.getValue(),
                        DatabaseCredentials.PASSWORD.getValue()
                )
                .cleanDisabled(false)
                .locations("db")
                .load();
        flyway.clean();
        flyway.migrate();
    }

    public static void initTestDatabase() {
        final Flyway flyway = Flyway.configure()
                .dataSource(
                        DatabaseCredentials.URL.getValue() + DatabaseCredentials.TEST_DATABASE_NAME.getValue(),
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