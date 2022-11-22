package ru.vk.dao;

import generatedEntities.tables.records.OrganisationRecord;
import org.jetbrains.annotations.NotNull;


import java.math.BigDecimal;
import java.sql.*;

import java.util.List;

import static generatedEntities.tables.Organisation.ORGANISATION;
import static generatedEntities.tables.Receipt.RECEIPT;
import static generatedEntities.tables.ReceiptItem.RECEIPT_ITEM;
import static org.jooq.impl.DSL.sum;

public final class OrganisationDAO extends AbstractDAO<OrganisationRecord, Long> {

    public OrganisationDAO(@NotNull Connection connection) {
        super(connection, ORGANISATION, ORGANISATION.TAX_NUMBER);
    }

    public List<OrganisationRecord> getTopTenOrganisationsByAmount() {
        return getContext().select(getTableName().fields()).from(getTableName()).innerJoin(RECEIPT).on(RECEIPT.ORGANISATION_TAX_NUMBER.eq(ORGANISATION.TAX_NUMBER)).innerJoin(RECEIPT_ITEM).on(RECEIPT.ID.eq(RECEIPT_ITEM.RECEIPT_ID)).groupBy(ORGANISATION.TAX_NUMBER).orderBy(sum(RECEIPT_ITEM.AMOUNT).desc()).limit(10).fetchInto(getTableName());
    }

    public List<OrganisationRecord> getOrganisationsWithByAmountMoreThanParameter(Integer productCode, Integer limit) {
        return getContext().select(getTableName().fields()).from(getTableName()).innerJoin(RECEIPT).on(RECEIPT.ORGANISATION_TAX_NUMBER.eq(ORGANISATION.TAX_NUMBER)).innerJoin(RECEIPT_ITEM).on(RECEIPT.ID.eq(RECEIPT_ITEM.RECEIPT_ID)).groupBy(ORGANISATION.TAX_NUMBER, RECEIPT_ITEM.PRODUCT_CODE).having(sum(RECEIPT_ITEM.AMOUNT).gt(BigDecimal.valueOf(limit)).and(RECEIPT_ITEM.PRODUCT_CODE.eq(productCode))).fetchInto(getTableName());
    }
}
