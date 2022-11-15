package ru.vk.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import ru.vk.JDBCCredentials;
import ru.vk.entities.Organisation;
import ru.vk.entities.Product;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ProductDAOTest extends AbstractTest {

    private final ProductDAO productDAO;

    private final ReceiptItemDAO receiptItemDAO;

    private final OrganisationDAO organisationDAO;

    ProductDAOTest() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBCCredentials.URL.getValue() + JDBCCredentials.TEST_DATABASE_NAME.getValue(), JDBCCredentials.LOGIN.getValue(), JDBCCredentials.PASSWORD.getValue());
        this.productDAO = new ProductDAO(connection);
        this.receiptItemDAO = new ReceiptItemDAO(connection);
        this.organisationDAO = new OrganisationDAO(connection);
    }


    @Test
    void getWhenProductCodeFound() {
        assertNotNull(productDAO.get(1L));
    }

    @Test
    void getWhenProductCodeNotFound() {
        assertThrows(IllegalStateException.class, () -> productDAO.get(54L));
    }

    @Test
    void getAll() {
        assertNotNull(productDAO.getAll());
    }

    @Test
    void deleteWhenIsReferencedByOtherEntities() {
        Long code = 1L;
        assertNotNull(productDAO.get(code));
        productDAO.delete(code);
        assertNotNull(productDAO.get(code));
    }

    @Test
    void deleteWhenIsNotReferencedByOtherEntities() {
        productDAO.delete(8L);
        assertThrows(IllegalStateException.class, () -> productDAO.get(8L));
    }

    @Test
    void update() {
        Long code = 2L;
        Product product = productDAO.get(code);
        String oldName = "Хлеб черный";
        assertEquals(product.getName(), oldName);
        product.setName("Хлеб шоколадный");
        productDAO.update(product);
        assertNotEquals(productDAO.get(code).getName(), oldName);

    }


    @Test
    void create() {
        try {
            Connection connection = DriverManager.getConnection(JDBCCredentials.URL.getValue() + JDBCCredentials.TEST_DATABASE_NAME.getValue(), JDBCCredentials.LOGIN.getValue(), JDBCCredentials.PASSWORD.getValue());
            Product product = new Product("Хлеб особенный");
            productDAO.create(product);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM products order by code desc");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                assertEquals(resultSet.getString("name"), product.getName());
            }
        } catch (SQLException e) {
            fail();
        }
    }


    @Test
    void getEveryDayProductStatsBetweenDates() {
        LocalDate from = LocalDate.of(2022, 11, 11);
        LocalDate to = LocalDate.of(2022, 11, 21);
        List<JsonObject> objects = productDAO.getEveryDayProductStatsBetweenDates(from, to);
        assertNotNull(objects);
        assertEquals(receiptItemDAO.getAll().size(), objects.size());
    }

    @Test
    void getAveragePriceOfEveryProductBetweenDates() {
        LocalDate from = LocalDate.of(2022, 11, 11);
        LocalDate to = LocalDate.of(2022, 11, 21);
        List<JsonObject> objects = productDAO.getAveragePriceOfEveryProductBetweenDates(from, to);
        assertNotNull(objects);
        List<Product> allProducts = productDAO.getAll();
        for (int index = 0; index<objects.size() - 1; index++){
            assertEquals(objects.get(index).get("productCode").getAsLong(), allProducts.get(index).getCode());
        }
    }

    @Test
    void getAllProductsBetweenDates() {
        LocalDate from = LocalDate.of(2022, 11, 11);
        LocalDate to = LocalDate.of(2022, 11, 21);
        List<JsonObject> objects = productDAO.getAllProductsBetweenDates(from, to);
        assertNotNull(objects);
        JsonObject organisationWithoutReceipts = objects.get(objects.size() - 1);
        assertInstanceOf(JsonNull.class, organisationWithoutReceipts.get("productName"));
        assertNotNull(organisationWithoutReceipts.get("organisationName"));
    }
}