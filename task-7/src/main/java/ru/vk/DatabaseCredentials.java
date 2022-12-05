package ru.vk;

public enum DatabaseCredentials {
    URL("jdbc:postgresql://localhost:5432/"),
    DATABASE_NAME("jettyDB"),
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
