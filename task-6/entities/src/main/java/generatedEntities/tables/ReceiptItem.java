/*
 * This file is generated by jOOQ.
 */
package generatedEntities.tables;


import generatedEntities.Keys;
import generatedEntities.Public;
import generatedEntities.tables.records.ReceiptItemRecord;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function5;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row5;
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
public class ReceiptItem extends TableImpl<ReceiptItemRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.receipt_item</code>
     */
    public static final ReceiptItem RECEIPT_ITEM = new ReceiptItem();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ReceiptItemRecord> getRecordType() {
        return ReceiptItemRecord.class;
    }

    /**
     * The column <code>public.receipt_item.id</code>.
     */
    public final TableField<ReceiptItemRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.receipt_item.receipt_id</code>.
     */
    public final TableField<ReceiptItemRecord, Integer> RECEIPT_ID = createField(DSL.name("receipt_id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.receipt_item.product_code</code>.
     */
    public final TableField<ReceiptItemRecord, Integer> PRODUCT_CODE = createField(DSL.name("product_code"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.receipt_item.price</code>.
     */
    public final TableField<ReceiptItemRecord, Integer> PRICE = createField(DSL.name("price"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.receipt_item.amount</code>.
     */
    public final TableField<ReceiptItemRecord, Integer> AMOUNT = createField(DSL.name("amount"), SQLDataType.INTEGER.nullable(false), this, "");

    private ReceiptItem(Name alias, Table<ReceiptItemRecord> aliased) {
        this(alias, aliased, null);
    }

    private ReceiptItem(Name alias, Table<ReceiptItemRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.receipt_item</code> table reference
     */
    public ReceiptItem(String alias) {
        this(DSL.name(alias), RECEIPT_ITEM);
    }

    /**
     * Create an aliased <code>public.receipt_item</code> table reference
     */
    public ReceiptItem(Name alias) {
        this(alias, RECEIPT_ITEM);
    }

    /**
     * Create a <code>public.receipt_item</code> table reference
     */
    public ReceiptItem() {
        this(DSL.name("receipt_item"), null);
    }

    public <O extends Record> ReceiptItem(Table<O> child, ForeignKey<O, ReceiptItemRecord> key) {
        super(child, key, RECEIPT_ITEM);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<ReceiptItemRecord, Integer> getIdentity() {
        return (Identity<ReceiptItemRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<ReceiptItemRecord> getPrimaryKey() {
        return Keys.RECEIPT_ITEM_PKEY;
    }

    @Override
    public List<ForeignKey<ReceiptItemRecord, ?>> getReferences() {
        return Arrays.asList(Keys.RECEIPT_ITEM__RECEIPT_ITEM_RECEIPT_ID_FKEY, Keys.RECEIPT_ITEM__RECEIPT_ITEM_PRODUCT_CODE_FKEY);
    }

    private transient Receipt _receipt;
    private transient Product _product;

    /**
     * Get the implicit join path to the <code>public.receipt</code> table.
     */
    public Receipt receipt() {
        if (_receipt == null)
            _receipt = new Receipt(this, Keys.RECEIPT_ITEM__RECEIPT_ITEM_RECEIPT_ID_FKEY);

        return _receipt;
    }

    /**
     * Get the implicit join path to the <code>public.product</code> table.
     */
    public Product product() {
        if (_product == null)
            _product = new Product(this, Keys.RECEIPT_ITEM__RECEIPT_ITEM_PRODUCT_CODE_FKEY);

        return _product;
    }

    @Override
    public ReceiptItem as(String alias) {
        return new ReceiptItem(DSL.name(alias), this);
    }

    @Override
    public ReceiptItem as(Name alias) {
        return new ReceiptItem(alias, this);
    }

    @Override
    public ReceiptItem as(Table<?> alias) {
        return new ReceiptItem(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public ReceiptItem rename(String name) {
        return new ReceiptItem(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ReceiptItem rename(Name name) {
        return new ReceiptItem(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public ReceiptItem rename(Table<?> name) {
        return new ReceiptItem(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<Integer, Integer, Integer, Integer, Integer> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function5<? super Integer, ? super Integer, ? super Integer, ? super Integer, ? super Integer, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function5<? super Integer, ? super Integer, ? super Integer, ? super Integer, ? super Integer, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}