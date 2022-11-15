package ru.vk.dao;

import org.junit.jupiter.api.Test;
import ru.vk.JDBCCredentials;
import ru.vk.entities.Organisation;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrganisationDAOTest extends AbstractTest {
    private final OrganisationDAO organisationDAO;

    public OrganisationDAOTest() throws SQLException {
        this.organisationDAO = new OrganisationDAO(DriverManager.getConnection(JDBCCredentials.URL.getValue() + JDBCCredentials.TEST_DATABASE_NAME.getValue(), JDBCCredentials.LOGIN.getValue(), JDBCCredentials.PASSWORD.getValue()));
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
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(JDBCCredentials.URL.getValue() + JDBCCredentials.TEST_DATABASE_NAME.getValue(), JDBCCredentials.LOGIN.getValue(), JDBCCredentials.PASSWORD.getValue());
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM organisations WHERE tax_number = ?");
            statement.setLong(1, organisation.getTaxNumber());
            ResultSet resultSet = statement.executeQuery();
            Long sizeBefore = 0L;
            while (resultSet.next()) {
                sizeBefore++;
            }
            organisationDAO.create(organisation);
            resultSet = statement.executeQuery();
            Long sizeAfter = 0L;
            while (resultSet.next()) {
                sizeAfter++;
            }
            assertTrue(sizeBefore < sizeAfter);
        } catch (SQLException e) {
            fail();
        }
    }

    @Test
    void getTopTenOrganisationsByAmount() {
        List<Organisation> objects = organisationDAO.getTopTenOrganisationsByAmount();
        assertNotNull(objects);
        List<Organisation> allOrganisations = organisationDAO.getAll();
        assertFalse(objects.contains(allOrganisations.get(allOrganisations.size()-1)));
    }

    @Test
    void getOrganisationsWithByAmountMoreThanParameter() {
        List<Organisation> objects = organisationDAO.getOrganisationsWithByAmountMoreThanParameter(10);
        assertNotNull(objects);
        assertEquals(10, objects.size());
        objects = organisationDAO.getOrganisationsWithByAmountMoreThanParameter(10000000);
        assertEquals(0, objects.size());
        objects = organisationDAO.getOrganisationsWithByAmountMoreThanParameter(90);
        assertEquals(3,objects.size());
    }
}