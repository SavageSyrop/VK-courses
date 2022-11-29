package ru.vk.dao;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.List;

public abstract class AbstractDAO<T> {

    private final @NotNull Connection connection;

    public AbstractDAO(@NotNull Connection connection) {
        this.connection = connection;
    }

    public abstract T get(Long pk);

    public abstract List<T> getAll();

    public abstract void delete(Long pk);

    public abstract void update(T object);

    public abstract void create(T object);

    @NotNull
    public Connection getConnection() {
        return connection;
    }
}
