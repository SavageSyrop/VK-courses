package ru.vk.dao;

import org.junit.jupiter.api.Test;
import ru.vk.JDBCCredentials;
import ru.vk.entities.Organisation;
import ru.vk.entities.Receipt;

import java.sql.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptDAOTest extends AbstractTest {
    private final ReceiptDAO receiptDAO;
    private final OrganisationDAO organisationDAO;

    public ReceiptDAOTest() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBCCredentials.URL.getValue() + JDBCCredentials.TEST_DATABASE_NAME.getValue(), JDBCCredentials.LOGIN.getValue(), JDBCCredentials.PASSWORD.getValue());
        this.receiptDAO = new ReceiptDAO(connection);
        this.organisationDAO = new OrganisationDAO(connection);
    }


    @Test
    void getWhenPrimaryKeyExists() {
        Long pk = 1L;
        assertNotNull(receiptDAO.get(pk));
    }

    @Test
    void getWhenPrimaryKeyDoesNotExist() {
        Long pk = 54L;
        assertThrows(IllegalStateException.class, () -> receiptDAO.get(pk));
    }

    @Test
    void getAll() {
        assertNotNull(receiptDAO.getAll());
    }

    @Test
    void deleteWhenIsReferencedByOtherEntities() {
        Long id = 1L;
        assertNotNull(receiptDAO.get(id));
        receiptDAO.delete(id);
        assertNotNull(receiptDAO.get(id));
    }

    @Test
    void deleteWhenIsNotReferencedByOtherEntities() {
        Long id = 11L;
        assertNotNull(receiptDAO.get(id));
        receiptDAO.delete(id);
        assertThrows(IllegalStateException.class, () -> receiptDAO.get(id));
    }

    @Test
    void update() {
        Long id = 1L;
        Receipt receipt = receiptDAO.get(id);
        LocalDate localDate = receipt.getCreationDate();
        assertEquals(receiptDAO.get(id).getCreationDate(), localDate);
        localDate = localDate.plusDays(1);
        receipt.setCreationDate(localDate);
        receiptDAO.update(receipt);
        assertNotEquals(receiptDAO.get(id).getCreationDate(), localDate);
    }

    @Test
    void create() {
        LocalDate localDate = LocalDate.now();
        Organisation organisation = organisationDAO.get(21632632L);
        Receipt receipt = new Receipt(localDate, organisation);
        receiptDAO.create(receipt);
        try {
            Connection connection = receiptDAO.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM receipts order by id desc");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                assertEquals(resultSet.getDate("creation_date").toLocalDate(), localDate);
            }
        } catch (SQLException e) {
            fail();
        }
    }
}