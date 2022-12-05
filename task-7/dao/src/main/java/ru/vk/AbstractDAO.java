package ru.vk;

import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;
import org.jooq.impl.UpdatableRecordImpl;

import java.sql.Connection;
import java.util.List;

public abstract class AbstractDAO<T extends UpdatableRecordImpl<T>, D extends Number> {

    private final @NotNull DSLContext context;

    private final @NotNull TableField<T, D> primaryKey;

    private final @NotNull TableImpl<T> tableName;

    public AbstractDAO(@NotNull Connection connection, @NotNull TableImpl<T> tableName, @NotNull TableField<T, D> primaryKey) {
        this.context = DSL.using(connection, SQLDialect.POSTGRES);
        this.tableName = tableName;
        this.primaryKey = primaryKey;
    }

    public T get(D pk) {
        T record = getContext().selectFrom(tableName).where(primaryKey.eq(pk)).fetchOne();
        if (record == null) {
            throw new IllegalStateException(tableName.getName() + "with primary key " + primaryKey.getName() + " equal to " + pk + " not found");
        }
        return record;
    }

    public List<T> getAll() {
        return getContext().selectFrom(tableName).fetch();
    }

    public void delete(D pk) {
        if (getContext().delete(tableName).where(primaryKey.eq(pk)).execute() == 0) {
            throw new IllegalStateException(tableName.getName() + "with primary key " + primaryKey.getName() + " equal to " + pk + " not found");
        }
    }

    public void update(T object) {
        D primaryKeyValue = object.getValue(primaryKey);
        if (primaryKeyValue == null) {
            throw new IllegalStateException(tableName.getName() + "has empty primary key " + primaryKey.getName());
        }


        if (context.executeUpdate(object) == 0) {
            throw new IllegalStateException(tableName.getName() + " with " + primaryKey + " equal to " + primaryKeyValue + " is not found");
        }
    }


    public void create(T object) {
        if (context.executeInsert(object) == 0) {
            throw new IllegalStateException("Error during insertion");
        }
    }

    @NotNull
    public DSLContext getContext() {
        return context;
    }


    @NotNull
    public TableImpl<T> getTableName() {
        return tableName;
    }
}
