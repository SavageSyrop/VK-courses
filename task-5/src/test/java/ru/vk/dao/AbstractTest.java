package ru.vk.dao;

import org.junit.jupiter.api.BeforeEach;
import ru.vk.FlywayInitializer;

public abstract class AbstractTest {
    @BeforeEach
    protected void initDB() {
        FlywayInitializer.initTestDatabase();
    }
}
