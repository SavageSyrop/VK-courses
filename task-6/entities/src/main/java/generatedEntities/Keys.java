/*
 * This file is generated by jOOQ.
 */
package generatedEntities;


import generatedEntities.tables.FlywaySchemaHistory;
import generatedEntities.tables.Organisation;
import generatedEntities.tables.Product;
import generatedEntities.tables.Receipt;
import generatedEntities.tables.ReceiptItem;
import generatedEntities.tables.records.FlywaySchemaHistoryRecord;
import generatedEntities.tables.records.OrganisationRecord;
import generatedEntities.tables.records.ProductRecord;
import generatedEntities.tables.records.ReceiptItemRecord;
import generatedEntities.tables.records.ReceiptRecord;

import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<FlywaySchemaHistoryRecord> FLYWAY_SCHEMA_HISTORY_PK = Internal.createUniqueKey(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, DSL.name("flyway_schema_history_pk"), new TableField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_RANK }, true);
    public static final UniqueKey<OrganisationRecord> ORGANISATION_CHECKING_ACCOUNT_KEY = Internal.createUniqueKey(Organisation.ORGANISATION, DSL.name("organisation_checking_account_key"), new TableField[] { Organisation.ORGANISATION.CHECKING_ACCOUNT }, true);
    public static final UniqueKey<OrganisationRecord> ORGANISATION_PKEY = Internal.createUniqueKey(Organisation.ORGANISATION, DSL.name("organisation_pkey"), new TableField[] { Organisation.ORGANISATION.TAX_NUMBER }, true);
    public static final UniqueKey<ProductRecord> PRODUCT_PK = Internal.createUniqueKey(Product.PRODUCT, DSL.name("product_pk"), new TableField[] { Product.PRODUCT.CODE }, true);
    public static final UniqueKey<ReceiptRecord> RECEIPT_PKEY = Internal.createUniqueKey(Receipt.RECEIPT, DSL.name("receipt_pkey"), new TableField[] { Receipt.RECEIPT.ID }, true);
    public static final UniqueKey<ReceiptItemRecord> RECEIPT_ITEM_PKEY = Internal.createUniqueKey(ReceiptItem.RECEIPT_ITEM, DSL.name("receipt_item_pkey"), new TableField[] { ReceiptItem.RECEIPT_ITEM.ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<ReceiptRecord, OrganisationRecord> RECEIPT__RECEIPT_ORGANISATION_TAX_NUMBER_FKEY = Internal.createForeignKey(Receipt.RECEIPT, DSL.name("receipt_organisation_tax_number_fkey"), new TableField[] { Receipt.RECEIPT.ORGANISATION_TAX_NUMBER }, Keys.ORGANISATION_PKEY, new TableField[] { Organisation.ORGANISATION.TAX_NUMBER }, true);
    public static final ForeignKey<ReceiptItemRecord, ProductRecord> RECEIPT_ITEM__RECEIPT_ITEM_PRODUCT_CODE_FKEY = Internal.createForeignKey(ReceiptItem.RECEIPT_ITEM, DSL.name("receipt_item_product_code_fkey"), new TableField[] { ReceiptItem.RECEIPT_ITEM.PRODUCT_CODE }, Keys.PRODUCT_PK, new TableField[] { Product.PRODUCT.CODE }, true);
    public static final ForeignKey<ReceiptItemRecord, ReceiptRecord> RECEIPT_ITEM__RECEIPT_ITEM_RECEIPT_ID_FKEY = Internal.createForeignKey(ReceiptItem.RECEIPT_ITEM, DSL.name("receipt_item_receipt_id_fkey"), new TableField[] { ReceiptItem.RECEIPT_ITEM.RECEIPT_ID }, Keys.RECEIPT_PKEY, new TableField[] { Receipt.RECEIPT.ID }, true);
}
