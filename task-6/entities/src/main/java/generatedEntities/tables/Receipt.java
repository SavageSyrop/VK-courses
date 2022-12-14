/*
 * This file is generated by jOOQ.
 */
package generatedEntities.tables;


import generatedEntities.Keys;
import generatedEntities.Public;
import generatedEntities.tables.records.ReceiptRecord;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function3;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row3;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Receipt extends TableImpl<ReceiptRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.receipt</code>
     */
    public static final Receipt RECEIPT = new Receipt();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ReceiptRecord> getRecordType() {
        return ReceiptRecord.class;
    }

    /**
     * The column <code>public.receipt.id</code>.
     */
    public final TableField<ReceiptRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.receipt.creation_date</code>.
     */
    public final TableField<ReceiptRecord, LocalDate> CREATION_DATE = createField(DSL.name("creation_date"), SQLDataType.LOCALDATE.nullable(false), this, "");

    /**
     * The column <code>public.receipt.organisation_tax_number</code>.
     */
    public final TableField<ReceiptRecord, Long> ORGANISATION_TAX_NUMBER = createField(DSL.name("organisation_tax_number"), SQLDataType.BIGINT.nullable(false), this, "");

    private Receipt(Name alias, Table<ReceiptRecord> aliased) {
        this(alias, aliased, null);
    }

    private Receipt(Name alias, Table<ReceiptRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.receipt</code> table reference
     */
    public Receipt(String alias) {
        this(DSL.name(alias), RECEIPT);
    }

    /**
     * Create an aliased <code>public.receipt</code> table reference
     */
    public Receipt(Name alias) {
        this(alias, RECEIPT);
    }

    /**
     * Create a <code>public.receipt</code> table reference
     */
    public Receipt() {
        this(DSL.name("receipt"), null);
    }

    public <O extends Record> Receipt(Table<O> child, ForeignKey<O, ReceiptRecord> key) {
        super(child, key, RECEIPT);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<ReceiptRecord, Integer> getIdentity() {
        return (Identity<ReceiptRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<ReceiptRecord> getPrimaryKey() {
        return Keys.RECEIPT_PKEY;
    }

    @Override
    public List<ForeignKey<ReceiptRecord, ?>> getReferences() {
        return Arrays.asList(Keys.RECEIPT__RECEIPT_ORGANISATION_TAX_NUMBER_FKEY);
    }

    private transient Organisation _organisation;

    /**
     * Get the implicit join path to the <code>public.organisation</code> table.
     */
    public Organisation organisation() {
        if (_organisation == null)
            _organisation = new Organisation(this, Keys.RECEIPT__RECEIPT_ORGANISATION_TAX_NUMBER_FKEY);

        return _organisation;
    }

    @Override
    public Receipt as(String alias) {
        return new Receipt(DSL.name(alias), this);
    }

    @Override
    public Receipt as(Name alias) {
        return new Receipt(alias, this);
    }

    @Override
    public Receipt as(Table<?> alias) {
        return new Receipt(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Receipt rename(String name) {
        return new Receipt(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Receipt rename(Name name) {
        return new Receipt(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Receipt rename(Table<?> name) {
        return new Receipt(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<Integer, LocalDate, Long> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function3<? super Integer, ? super LocalDate, ? super Long, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function3<? super Integer, ? super LocalDate, ? super Long, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
