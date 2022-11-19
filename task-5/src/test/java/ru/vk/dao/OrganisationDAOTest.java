package ru.vk.dao;

import org.junit.jupiter.api.Test;
import ru.vk.JDBCCredentials;
import ru.vk.entities.Organisation;
import ru.vk.entities.Receipt;
import ru.vk.entities.ReceiptItem;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class OrganisationDAOTest extends AbstractTest {
    private final OrganisationDAO organisationDAO;
    private final ReceiptDAO receiptDAO;
    private final ReceiptItemDAO receiptItemDAO;

    public OrganisationDAOTest() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBCCredentials.URL.getValue() + JDBCCredentials.TEST_DATABASE_NAME.getValue(), JDBCCredentials.LOGIN.getValue(), JDBCCredentials.PASSWORD.getValue());
        this.organisationDAO = new OrganisationDAO(connection);
        this.receiptDAO = new ReceiptDAO(connection);
        this.receiptItemDAO = new ReceiptItemDAO(connection);
    }


    @Test
    void getWhenPrimaryKeyFound() {
        Long taxNumber = 21632632L;
        assertNotNull(organisationDAO.get(taxNumber));
    }


    @Test
    void getWhenPrimaryKeyNotFound() {
        Long taxNumber = 228322L;
        assertThrows(IllegalStateException.class, () -> organisationDAO.get(taxNumber));
    }


    @Test
    void getAll() {
        assertNotNull(organisationDAO.getAll());
    }

    @Test
    void deleteWhenIsReferencedByOtherEntities() {
        Long taxNumber = 21632632L;
        organisationDAO.delete(taxNumber);
        assertNotNull(organisationDAO.get(taxNumber));
    }

    @Test
    void deleteWhenIsNotReferencedByOtherEntities() {
        Long taxNumber = 64392994L;
        assertNotNull(organisationDAO.get(taxNumber));
        organisationDAO.delete(taxNumber);
        assertThrows(IllegalStateException.class, () -> organisationDAO.get(taxNumber));
    }

    @Test
    void update() {
        Long taxNumber = 64392994L;
        Organisation organisation = organisationDAO.get(taxNumber);
        String oldName = organisation.getName();
        organisation.setName("Хабаровский хлебокомбинат");
        organisationDAO.update(organisation);
        assertEquals(oldName, organisationDAO.get(taxNumber).getName());
    }

    @Test
    void createWhenDoesNotExist() {
        Organisation organisation = new Organisation(95129898L, "Боровский хлебокомбинат", 97853299L);
        Connection connection = organisationDAO.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM organisations WHERE tax_number = ?");
            statement.setLong(1, organisation.getTaxNumber());
            ResultSet resultSet = statement.executeQuery();
            Long sizeBefore = 0L;
            while (resultSet.next()) {
                sizeBefore++;
            }
            assertThrows(IllegalStateException.class, () -> organisationDAO.get(organisation.getTaxNumber()));
            organisationDAO.create(organisation);
            resultSet = statement.executeQuery();
            Long sizeAfter = 0L;
            while (resultSet.next()) {
                sizeAfter++;
            }
            assertTrue(sizeBefore < sizeAfter);
            assertNotNull(organisationDAO.get(organisation.getTaxNumber()));
        } catch (SQLException e) {
            fail();
        }
    }

    @Test
    void getTopTenOrganisationsByAmount() {
        Map<Long, Integer> amountsByOrganisationTaxNumber = new HashMap<>();
        for (ReceiptItem receiptItem : receiptItemDAO.getAll()) {
            Receipt receipt = receiptDAO.get(receiptItem.getReceiptId());
            Long organisationTaxNumber = receipt.getOrganisation().getTaxNumber();
            Integer currentAmount = amountsByOrganisationTaxNumber.get(organisationTaxNumber);
            if (currentAmount == null) {
                currentAmount = 0;
            }
            currentAmount += receiptItem.getAmount();
            amountsByOrganisationTaxNumber.put(organisationTaxNumber, currentAmount);
        }
        Set<Map.Entry<Long, Integer>> entries = amountsByOrganisationTaxNumber.entrySet();
        List<Long> topOrganisations = entries.stream().sorted(Map.Entry.<Long, Integer>comparingByValue().reversed()).limit(10).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new)).keySet().stream().toList();
        List<Organisation> queryResult = organisationDAO.getTopTenOrganisationsByAmount();
        for (Organisation organisation : queryResult) {
            assertTrue(topOrganisations.contains(organisation.getTaxNumber()));
        }
    }

    @Test
    void getOrganisationsWithByAmountMoreThanParameter() {
        Integer limit = 90;
        Map<Long, Integer> amountsByOrganisationTaxNumber = new HashMap<>();
        for (ReceiptItem receiptItem : receiptItemDAO.getAll()) {
            Receipt receipt = receiptDAO.get(receiptItem.getReceiptId());
            Long organisationTaxNumber = receipt.getOrganisation().getTaxNumber();
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
        List<Organisation> queryResult = organisationDAO.getOrganisationsWithByAmountMoreThanParameter(1, 90);
        for (Organisation organisation : queryResult) {
            assertTrue(foundOrganisations.contains(organisation.getTaxNumber()));
        }
    }
}