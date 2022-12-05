package ru.vk.dao;

import generatedEntities.tables.records.ReceiptRecord;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

import static generatedEntities.tables.Receipt.RECEIPT;

public final class ReceiptDAO extends AbstractDAO<ReceiptRecord, Integer> {
    public ReceiptDAO(@NotNull Connection connection) {
        super(connection, RECEIPT, RECEIPT.ID);
    }
}
