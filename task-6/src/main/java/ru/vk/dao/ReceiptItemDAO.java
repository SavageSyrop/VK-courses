package ru.vk.dao;

import generatedEntities.tables.records.ReceiptItemRecord;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

import static generatedEntities.tables.ReceiptItem.RECEIPT_ITEM;

public final class ReceiptItemDAO extends AbstractDAO<ReceiptItemRecord, Integer> {
    public ReceiptItemDAO(@NotNull Connection connection) {
        super(connection, RECEIPT_ITEM, RECEIPT_ITEM.ID);
    }
}
