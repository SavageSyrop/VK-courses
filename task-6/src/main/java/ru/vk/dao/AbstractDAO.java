package ru.vk.dao;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;
import org.jooq.impl.UpdatableRecordImpl;
import ru.vk.DatabaseCredentials;

import java.sql.Connection;
import java.util.List;

public abstract class AbstractDAO<VALUE extends UpdatableRecordImpl<VALUE>, KEY extends Number> {

    private final @NotNull DSLContext context;

    private final @NotNull TableField<VALUE, KEY> primaryKey;

    private final @NotNull TableImpl<VALUE> tableName;

    public AbstractDAO(@NotNull Connection connection, @NotNull TableImpl<VALUE> tableName, @NotNull TableField<VALUE, KEY> primaryKey) {
        this.context = DSL.using(connection, SQLDialect.valueOf(DatabaseCredentials.SQL_DIALECT.getValue()));
        this.tableName = tableName;
        this.primaryKey = primaryKey;
    }


    public VALUE get(@NotNull KEY pk) {
        VALUE record = getContext().selectFrom(tableName).where(primaryKey.eq(pk)).fetchOne();
        if (record == null) {
            throw new IllegalStateException(tableName.getName() + "with primary key " + primaryKey.getName() + " equal to " + pk + " not found");
        }
        return record;
    }

    public List<VALUE> getAll() {
        return getContext().selectFrom(tableName).fetch();
    }

    public void delete(@NotNull KEY pk) {
        if (getContext().delete(tableName).where(primaryKey.eq(pk)).execute() == 0) {
            throw new IllegalStateException(tableName.getName() + "with primary key " + primaryKey.getName() + " equal to " + pk + " not found");
        }
    }

    public void update(@NotNull VALUE object) {
        final KEY primaryKeyValue = object.getValue(primaryKey);
        if (primaryKeyValue == null) {
            throw new IllegalStateException(tableName.getName() + "has empty primary key " + primaryKey.getName());
        }


        if (context.executeUpdate(object) == 0) {
            throw new IllegalStateException(tableName.getName() + " with " + primaryKey + " equal to " + primaryKeyValue + " is not found");
        }
    }


    public void create(@NotNull VALUE object) {
        if (context.executeInsert(object) == 0) {
            throw new IllegalStateException("Error during insertion");
        }
    }

    @NotNull
    public DSLContext getContext() {
        return context;
    }



    @NotNull
    public TableImpl<VALUE> getTableName() {
        return tableName;
    }
}
