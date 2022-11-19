package ru.vk.dao;

import org.junit.jupiter.api.Test;
import ru.vk.JDBCCredentials;
import ru.vk.entities.Product;
import ru.vk.entities.ReceiptItem;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptItemDAOTest extends AbstractTest {
    private final ReceiptItemDAO receiptItemDAO;
    private final ProductDAO productDAO;

    public ReceiptItemDAOTest() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBCCredentials.URL.getValue() + JDBCCredentials.TEST_DATABASE_NAME.getValue(), JDBCCredentials.LOGIN.getValue(), JDBCCredentials.PASSWORD.getValue());
        this.receiptItemDAO = new ReceiptItemDAO(connection);
        this.productDAO = new ProductDAO(connection);

    }

    @Test
    void getWhenPrimaryKeyNotFound() {
        Long pk = 54L;
        assertThrows(IllegalStateException.class, () -> receiptItemDAO.get(pk));
    }

    @Test
    void getWhenPrimaryKeyFound() {
        Long pk = 1L;
        assertNotNull(receiptItemDAO.get(pk));
    }

    @Test
    void getAll() {
        assertNotNull(receiptItemDAO.getAll());
    }

    @Test
    void delete() {
        Long pk = 1L;
        assertNotNull(receiptItemDAO.get(pk));
        receiptItemDAO.delete(pk);
        assertThrows(IllegalStateException.class, () -> receiptItemDAO.get(pk));
    }

    @Test
    void update() {
        Long pk = 1L;
        ReceiptItem receiptItem = receiptItemDAO.get(1L);
        Integer oldAmount = receiptItem.getAmount();
        receiptItem.setAmount(10);
        receiptItemDAO.update(receiptItem);
        assertNotEquals(oldAmount, receiptItemDAO.get(pk).getAmount());
    }

    @Test
    void create() {
        try {
            Connection connection = receiptItemDAO.getConnection();
            Product product = productDAO.get(1L);
            Long receiptId = 10L;
            ReceiptItem receiptItem = new ReceiptItem(receiptId, product, 100, 200);
            receiptItemDAO.create(receiptItem);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM receipt_items order by id desc");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                assertEquals(resultSet.getInt("price"), receiptItem.getPrice());
                assertEquals(resultSet.getInt("amount"), receiptItem.getAmount());
                assertEquals(resultSet.getLong("product_code"), product.getCode());
                assertEquals(resultSet.getLong("receipt_id"), receiptId);
            }
        } catch (SQLException e) {
            fail();
        }
    }
}