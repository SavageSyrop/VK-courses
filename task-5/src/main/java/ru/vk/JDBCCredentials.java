package ru.vk;

import lombok.Getter;

@Getter
public enum JDBCCredentials {
    URL("jdbc:postgresql://localhost:5432/"),
    DATABASE_NAME("jdbcDB"),
    TEST_DATABASE_NAME("jdbcTestDB"),
    LOGIN("postgres"),
    PASSWORD("postgres");

    private final String value;

    JDBCCredentials(String value) {
        this.value = value;
    }
}
