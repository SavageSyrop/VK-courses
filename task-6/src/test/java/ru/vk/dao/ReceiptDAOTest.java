package ru.vk.dao;

import generatedEntities.tables.records.ReceiptRecord;
import org.jooq.exception.DataAccessException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static generatedEntities.tables.Receipt.RECEIPT;
import static org.junit.jupiter.api.Assertions.*;

class ReceiptDAOTest extends AbstractTest {
    private final ReceiptDAO receiptDAO;


    public ReceiptDAOTest() {
        this.receiptDAO = new ReceiptDAO(getConnection());
    }


    @Test
    void getWhenPrimaryKeyExists() {
        int pk = 1;
        assertNotNull(receiptDAO.get(pk));
    }

    @Test
    void getWhenPrimaryKeyDoesNotExist() {
        int pk = 54;
        assertThrows(IllegalStateException.class, () -> receiptDAO.get(pk));
    }

    @Test
    void getAll() {
        assertNotNull(receiptDAO.getAll());
    }

    @Test
    void deleteWhenIsReferencedByOtherEntities() {
        int id = 1;
        assertThrows(DataAccessException.class, () -> receiptDAO.delete(id));
        assertNotNull(receiptDAO.get(id));
    }

    @Test
    void deleteWhenIsNotReferencedByOtherEntities() {
        int id = 11;
        assertNotNull(receiptDAO.get(id));
        receiptDAO.delete(id);
        assertThrows(IllegalStateException.class, () -> receiptDAO.get(id));
    }

    @Test
    void update() {
        int id = 1;
        ReceiptRecord receipt = receiptDAO.get(id);
        LocalDate localDate = receipt.getCreationDate();
        assertEquals(receiptDAO.get(id).getCreationDate(), localDate);
        LocalDate newDate = localDate.plusDays(1);
        receipt.setCreationDate(newDate);
        receiptDAO.update(receipt);
        assertEquals(receiptDAO.get(id).getCreationDate(), newDate);
    }

    @Test
    void create() {
        LocalDate creationTime = LocalDate.now();
        Long organisationTaxNumber = 21632632L;
        ReceiptRecord receipt = new ReceiptRecord();
        receipt.setCreationDate(creationTime);
        receipt.setOrganisationTaxNumber(organisationTaxNumber);
        int sizeBefore = receiptDAO.getAll().size();
        receiptDAO.create(receipt);
        int sizeAfter = receiptDAO.getAll().size();
        assertTrue(sizeBefore < sizeAfter);
        ReceiptRecord createdReceipt = receiptDAO.get(sizeAfter);
        assertNotNull(createdReceipt);
        assertEquals(createdReceipt.get(RECEIPT.CREATION_DATE), creationTime);
        assertEquals(createdReceipt.get(RECEIPT.ORGANISATION_TAX_NUMBER), organisationTaxNumber);
    }
}