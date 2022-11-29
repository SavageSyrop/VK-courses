package ru.vk.dao;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import ru.vk.JDBCCredentials;
import ru.vk.entities.Organisation;
import ru.vk.entities.Product;
import ru.vk.entities.Receipt;
import ru.vk.entities.ReceiptItem;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ProductDAOTest extends AbstractTest {

    private final ProductDAO productDAO;

    private final ReceiptItemDAO receiptItemDAO;

    private final OrganisationDAO organisationDAO;

    private final ReceiptDAO receiptDAO;

    ProductDAOTest() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBCCredentials.URL.getValue() + JDBCCredentials.TEST_DATABASE_NAME.getValue(), JDBCCredentials.LOGIN.getValue(), JDBCCredentials.PASSWORD.getValue());
        this.productDAO = new ProductDAO(connection);
        this.receiptItemDAO = new ReceiptItemDAO(connection);
        this.organisationDAO = new OrganisationDAO(connection);
        this.receiptDAO = new ReceiptDAO(connection);
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
            Connection connection = productDAO.getConnection();
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

        Integer queryTotalPrice = 0;
        Integer codeTotalPrice = 0;

        List<JsonObject> objects = productDAO.getEveryDayProductStatsBetweenDates(from, to);
        for (JsonObject object : objects) {
            LocalDate creationDate = LocalDate.parse(object.get("creationDate").getAsString());
            queryTotalPrice += object.get("totalPrice").getAsInt();
            assertTrue(from.equals(creationDate) || creationDate.equals(to) || (from.isBefore(creationDate) && to.isAfter(creationDate)));
        }

        for (ReceiptItem item : receiptItemDAO.getAll()) {
            codeTotalPrice += item.getPrice() * item.getAmount();
        }
        assertEquals(codeTotalPrice, queryTotalPrice);
    }

    @Test
    void getAveragePriceOfEveryProductBetweenDates() {
        LocalDate from = LocalDate.of(2022, 11, 11);
        LocalDate to = LocalDate.of(2022, 11, 21);

        Map<Long, Double> averagePriceForEveryProduct = new TreeMap<>();
        Map<Long, Integer> productCount = new HashMap<>();
        for (ReceiptItem receiptItem : receiptItemDAO.getAll()) {
            Long productCode = receiptItem.getProduct().getCode();
            Double productPriceSum = averagePriceForEveryProduct.get(productCode);
            if (productPriceSum == null) {
                productPriceSum = 0D;
            }
            productPriceSum += receiptItem.getPrice() * receiptItem.getAmount();
            averagePriceForEveryProduct.put(productCode, productPriceSum);
            Integer prCount = productCount.get(productCode);
            if (prCount == null) {
                prCount = 0;
            }
            prCount += receiptItem.getAmount();
            productCount.put(productCode, prCount);
        }
        for (Map.Entry<Long, Double> element : averagePriceForEveryProduct.entrySet()) {
            averagePriceForEveryProduct.put(element.getKey(), element.getValue() / productCount.get(element.getKey()));
        }

        List<Double> productTotalPrices = averagePriceForEveryProduct.values().stream().toList();
        List<JsonObject> objects = productDAO.getAveragePriceOfEveryProductBetweenDates(from, to);

        for (int index = 0; index < objects.size() - 1; index++) {
            assertEquals(Double.valueOf(objects.get(index).get("averagePrice").getAsDouble()), productTotalPrices.get(index));
        }
    }

    @Test
    void getAllProductsBetweenDates() {
        LocalDate from = LocalDate.of(2022, 11, 11);
        LocalDate to = LocalDate.of(2022, 11, 21);
        Map<Long, Boolean> organisationHasProducts = new HashMap<>();
        for (Organisation organisation : organisationDAO.getAll()) {
            organisationHasProducts.put(organisation.getTaxNumber(), false);
        }
        Map<Long, List<Long>> productsByOrganisation = new HashMap<>();
        for (ReceiptItem receiptItem : receiptItemDAO.getAll()) {
            Receipt receipt = receiptDAO.get(receiptItem.getReceiptId());
            LocalDate creationDate = receipt.getCreationDate();
            Long organisationTaxNumber = receipt.getOrganisation().getTaxNumber();
            if (from.equals(creationDate) || creationDate.equals(to) || (from.isBefore(creationDate) && to.isAfter(creationDate))) {
                if (!productsByOrganisation.containsKey(organisationTaxNumber)) {
                    productsByOrganisation.put(organisationTaxNumber, new ArrayList<>());
                    organisationHasProducts.put(organisationTaxNumber, true);
                }
                productsByOrganisation.get(organisationTaxNumber).add(receiptItem.getProduct().getCode());
            }
        }

        List<JsonObject> objects = productDAO.getAllProductsBetweenDates(from, to);
        for (JsonObject object : objects) {
            Long productCode = object.get("productCode").getAsLong();
            Long organisationTaxNumber = object.get("taxNumber").getAsLong();
            if (productCode == 0) {
                assertFalse(productsByOrganisation.containsKey(organisationTaxNumber));
            } else {
                assertTrue(organisationHasProducts.get(organisationTaxNumber));
                assertTrue(productsByOrganisation.get(organisationTaxNumber).contains(productCode));
            }
        }
    }
}