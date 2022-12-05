package ru.vk.dao;


import generatedEntities.tables.records.ProductRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.Record2;
import org.jooq.Record4;
import org.jooq.Record5;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import static generatedEntities.tables.Organisation.ORGANISATION;
import static generatedEntities.tables.Product.PRODUCT;
import static generatedEntities.tables.Receipt.RECEIPT;
import static generatedEntities.tables.ReceiptItem.RECEIPT_ITEM;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.sum;

public class ProductDAO extends AbstractDAO<ProductRecord, Integer> {
    public ProductDAO(@NotNull Connection connection) {
        super(connection, PRODUCT, PRODUCT.CODE);
    }


    public List<Record5<LocalDate, Integer, Integer, Integer, Integer>> getEveryDayProductStatsBetweenDates(@NotNull LocalDate from, @NotNull LocalDate to) {
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("Wrong dates: period end is before period start");
        }

        return getContext().select(
                        RECEIPT.CREATION_DATE,
                        RECEIPT_ITEM.PRODUCT_CODE,
                        cast(sum(RECEIPT_ITEM.AMOUNT), Integer.class).as("total_amount"),
                        RECEIPT_ITEM.PRICE,
                        cast(sum(RECEIPT_ITEM.AMOUNT.mul(RECEIPT_ITEM.PRICE)), Integer.class).as("total_price"))
                .from(RECEIPT_ITEM)
                .innerJoin(RECEIPT).on(RECEIPT_ITEM.RECEIPT_ID.eq(RECEIPT.ID))
                .where(RECEIPT.CREATION_DATE.between(from, to))
                .groupBy(RECEIPT.CREATION_DATE, RECEIPT_ITEM.PRODUCT_CODE, RECEIPT_ITEM.PRICE)
                .orderBy(RECEIPT.CREATION_DATE)
                .fetch();
    }

    public List<Record2<Integer, BigDecimal>> getAveragePriceOfEveryProductBetweenDates(@NotNull LocalDate from, @NotNull LocalDate to) {
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("Wrong dates: period end is before period start");
        }

        return getContext().select(
                        RECEIPT_ITEM.PRODUCT_CODE,
                        cast(sum(RECEIPT_ITEM.PRICE.mul(RECEIPT_ITEM.AMOUNT)), BigDecimal.class).div(cast(sum(RECEIPT_ITEM.AMOUNT), BigDecimal.class)).as("average_price"))
                .from(RECEIPT_ITEM)
                .innerJoin(RECEIPT).on(RECEIPT_ITEM.RECEIPT_ID.eq(RECEIPT.ID))
                .where(RECEIPT.CREATION_DATE.between(from, to))
                .groupBy(RECEIPT_ITEM.PRODUCT_CODE)
                .fetch();
    }

    public List<Record4<Integer, String, String, Long>> getAllProductsBetweenDates(@NotNull LocalDate from, @NotNull LocalDate to) {
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("Wrong dates: period end is before period start");
        }

        List<Record4<Integer, String, String, Long>> result = getContext().select(
                        RECEIPT_ITEM.PRODUCT_CODE,
                        PRODUCT.NAME.as("product_name"),
                        ORGANISATION.NAME.as("organisation_name"),
                        ORGANISATION.TAX_NUMBER)
                .from(RECEIPT_ITEM)
                .innerJoin(PRODUCT).on(RECEIPT_ITEM.PRODUCT_CODE.eq(PRODUCT.CODE))
                .innerJoin(RECEIPT).on(RECEIPT.ID.eq(RECEIPT_ITEM.RECEIPT_ID))
                .rightJoin(ORGANISATION).on(ORGANISATION.TAX_NUMBER.eq(RECEIPT.ORGANISATION_TAX_NUMBER))
                .where((RECEIPT.CREATION_DATE.isNull()).or(RECEIPT.CREATION_DATE.between(from, to)))
                .fetch();
        return result;
    }
}
