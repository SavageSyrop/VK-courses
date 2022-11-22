package ru.vk.dao;

import generatedEntities.tables.records.ReceiptItemRecord;
import org.junit.jupiter.api.Test;

import static generatedEntities.tables.ReceiptItem.RECEIPT_ITEM;
import static org.junit.jupiter.api.Assertions.*;

class ReceiptItemDAOTest extends AbstractTest {
    private final ReceiptItemDAO receiptItemDAO;


    public ReceiptItemDAOTest() {
        this.receiptItemDAO = new ReceiptItemDAO(getConnection());
    }

    @Test
    void getWhenPrimaryKeyNotFound() {
        Integer pk = 54;
        assertThrows(IllegalStateException.class, () -> receiptItemDAO.get(pk));
    }

    @Test
    void getWhenPrimaryKeyFound() {
        Integer pk = 1;
        assertNotNull(receiptItemDAO.get(pk));
    }

    @Test
    void getAll() {
        assertNotNull(receiptItemDAO.getAll());
    }

    @Test
    void delete() {
        Integer pk = 1;
        assertNotNull(receiptItemDAO.get(pk));
        receiptItemDAO.delete(pk);
        assertThrows(IllegalStateException.class, () -> receiptItemDAO.get(pk));
    }

    @Test
    void update() {
        Integer pk = 1;
        ReceiptItemRecord receiptItem = receiptItemDAO.get(pk);
        Integer oldAmount = receiptItem.getAmount();
        receiptItem.setAmount(10);
        receiptItemDAO.update(receiptItem);
        assertNotEquals(oldAmount, receiptItemDAO.get(pk).getAmount());
    }

    @Test
    void create() {
        Integer productCode = 1;
        Integer receiptId = 10;
        ReceiptItemRecord receiptItem = new ReceiptItemRecord();
        receiptItem.setReceiptId(receiptId);
        receiptItem.setAmount(200);
        receiptItem.setPrice(100);
        receiptItem.setProductCode(productCode);
        Integer sizeBefore = receiptItemDAO.getAll().size();
        assertThrows(IllegalStateException.class, () -> receiptItemDAO.get(sizeBefore + 1));
        receiptItemDAO.create(receiptItem);
        Integer sizeAfter = receiptItemDAO.getAll().size();
        assertTrue(sizeBefore < sizeAfter);
        ReceiptItemRecord receiptItemRecord = receiptItemDAO.get(sizeAfter);
        assertEquals(receiptItemRecord.get(RECEIPT_ITEM.RECEIPT_ID), receiptItem.getReceiptId());
        assertEquals(receiptItemRecord.get(RECEIPT_ITEM.AMOUNT), receiptItem.getAmount());
        assertEquals(receiptItemRecord.get(RECEIPT_ITEM.PRICE), receiptItem.getPrice());
        assertEquals(receiptItemRecord.get(RECEIPT_ITEM.PRODUCT_CODE), receiptItem.getProductCode());
    }
}