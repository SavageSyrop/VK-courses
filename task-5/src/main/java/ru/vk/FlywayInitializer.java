package ru.vk;

import org.flywaydb.core.Flyway;

public final class FlywayInitializer {
    public static void initDatabase() {
        final Flyway flyway = Flyway.configure()
                .dataSource(
                        JDBCCredentials.URL.getValue() + JDBCCredentials.DATABASE_NAME.getValue(),
                        JDBCCredentials.LOGIN.getValue(),
                        JDBCCredentials.PASSWORD.getValue()
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
                        JDBCCredentials.URL.getValue() + JDBCCredentials.TEST_DATABASE_NAME.getValue(),
                        JDBCCredentials.LOGIN.getValue(),
                        JDBCCredentials.PASSWORD.getValue()
                )
                .cleanDisabled(false)
                .locations("db")
                .load();
        flyway.clean();
        flyway.migrate();
    }
}