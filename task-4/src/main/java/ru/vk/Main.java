package ru.vk;

import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;

public class Main {
    public static final @NotNull String CONNECTION = "jdbc:postgresql://localhost:5432/";
    public static final @NotNull String DB_NAME = "sunshineDatabase";
    public static final @NotNull String USERNAME = "postgres";
    public static final @NotNull String PASSWORD = "postgres";

    public static void main(String[] args) {
        final Flyway flyway = Flyway
                .configure()
                .dataSource(CONNECTION + DB_NAME, USERNAME, PASSWORD)
                .locations("db")
                .cleanDisabled(false)
                .load();
        flyway.clean();
        flyway.migrate();
        System.out.println("Migrations applied successfully");
    }
}