package ru.vk.dao;

import generatedEntities.tables.records.OrganisationRecord;
import generatedEntities.tables.records.ReceiptItemRecord;
import generatedEntities.tables.records.ReceiptRecord;
import org.jooq.exception.DataAccessException;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class OrganisationDAOTest extends AbstractTest {
    private final OrganisationDAO organisationDAO;
    private final ReceiptDAO receiptDAO;
    private final ReceiptItemDAO receiptItemDAO;

    public OrganisationDAOTest() {
        this.organisationDAO = new OrganisationDAO(getConnection());
        this.receiptDAO = new ReceiptDAO(getConnection());
        this.receiptItemDAO = new ReceiptItemDAO(getConnection());
    }


    @Test
    void getWhenPrimaryKeyFound() {
        long taxNumber = 21632632;
        assertNotNull(organisationDAO.get(taxNumber));
    }


    @Test
    void getWhenPrimaryKeyNotFound() {
        long taxNumber = 228322;
        assertThrows(IllegalStateException.class, () -> organisationDAO.get(taxNumber));
    }


    @Test
    void getAll() {
        assertNotNull(organisationDAO.getAll());
    }

    @Test
    void deleteWhenIsReferencedByOtherEntities() {
        long taxNumber = 21632632;
        assertThrows(DataAccessException.class, () -> organisationDAO.delete(taxNumber));
        assertNotNull(organisationDAO.get(taxNumber));
    }

    @Test
    void deleteWhenIsNotReferencedByOtherEntities() {
        long taxNumber = 64392994;
        assertNotNull(organisationDAO.get(taxNumber));
        organisationDAO.delete(taxNumber);
        assertThrows(IllegalStateException.class, () -> organisationDAO.get(taxNumber));
    }

    @Test
    void update() {
        long taxNumber = 64392994;
        OrganisationRecord organisation = organisationDAO.get(taxNumber);
        String oldName = organisation.getName();
        organisation.setName("Хабаровский хлебокомбинат");
        organisationDAO.update(organisation);
        assertNotEquals(oldName, organisationDAO.get(taxNumber).getName());
    }

    @Test
    void createWhenDoesNotExist() {
        OrganisationRecord organisation = new OrganisationRecord(95129898L, "Боровский хлебокомбинат", 97853299L);
        assertThrows(IllegalStateException.class, () -> organisationDAO.get(organisation.getTaxNumber()));
        organisationDAO.create(organisation);
        assertNotNull(organisationDAO.get(organisation.getTaxNumber()));
    }

    @Test
    void getTopTenOrganisationsByAmount() {
        Map<Long, Integer> amountsByOrganisationTaxNumber = new HashMap<>();
        for (ReceiptItemRecord receiptItem : receiptItemDAO.getAll()) {
            ReceiptRecord receipt = receiptDAO.get(receiptItem.getReceiptId());
            Long organisationTaxNumber = receipt.getOrganisationTaxNumber();
            Integer currentAmount = amountsByOrganisationTaxNumber.get(organisationTaxNumber);
            if (currentAmount == null) {
                currentAmount = 0;
            }
            currentAmount += receiptItem.getAmount();
            amountsByOrganisationTaxNumber.put(organisationTaxNumber, currentAmount);
        }
        Set<Map.Entry<Long, Integer>> entries = amountsByOrganisationTaxNumber.entrySet();
        List<Long> topOrganisations = entries.stream().sorted(Map.Entry.<Long, Integer>comparingByValue().reversed()).limit(10).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new)).keySet().stream().toList();
        List<OrganisationRecord> queryResult = organisationDAO.getTopTenOrganisationsByAmount();
        for (OrganisationRecord organisation : queryResult) {
            assertTrue(topOrganisations.contains(organisation.getTaxNumber()));
        }
    }

    @Test
    void getOrganisationsWithByAmountMoreThanParameter() {
        int limit = 90;
        Map<Long, Integer> amountsByOrganisationTaxNumber = new HashMap<>();
        for (ReceiptItemRecord receiptItem : receiptItemDAO.getAll()) {
            ReceiptRecord receipt = receiptDAO.get(receiptItem.getReceiptId());
            Long organisationTaxNumber = receipt.getOrganisationTaxNumber();
            Integer currentAmount = amountsByOrganisationTaxNumber.get(organisationTaxNumber);
            if (currentAmount == null) {
                currentAmount = 0;
            }
            currentAmount += receiptItem.getAmount();
            if (currentAmount > limit) {
                amountsByOrganisationTaxNumber.put(organisationTaxNumber, currentAmount);
            }
        }
        Set<Map.Entry<Long, Integer>> entries = amountsByOrganisationTaxNumber.entrySet();
        List<Long> foundOrganisations = entries.stream().sorted(Map.Entry.<Long, Integer>comparingByValue().reversed()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new)).keySet().stream().toList();
        List<OrganisationRecord> queryResult = organisationDAO.getOrganisationsWithByAmountMoreThanParameter(1, 90);
        for (OrganisationRecord organisation : queryResult) {
            assertTrue(foundOrganisations.contains(organisation.getTaxNumber()));
        }
    }
}