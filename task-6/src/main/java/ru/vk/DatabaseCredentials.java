package ru.vk;

import org.jooq.SQLDialect;

public enum DatabaseCredentials {
    URL("jdbc:postgresql://localhost:5432/"),
    DATABASE_NAME("jdbcDB"),
    TEST_DATABASE_NAME("jdbcTestDB"),
    SQL_DIALECT(SQLDialect.POSTGRES.toString()),
    LOGIN("postgres"),
    PASSWORD("postgres");

    private final String value;

    DatabaseCredentials(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
