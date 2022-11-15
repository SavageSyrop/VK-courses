package ru.vk.dao;

import org.jetbrains.annotations.NotNull;
import ru.vk.entities.Product;
import ru.vk.entities.ReceiptItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class ReceiptItemDAO extends AbstractDAO<ReceiptItem> {
    public ReceiptItemDAO(@NotNull Connection connection) {
        super(connection);
    }

    @Override
    public ReceiptItem get(Long pk) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM receipt_items WHERE id = " + pk);
            if (resultSet.next()) {
                Long productCode = resultSet.getLong("product_code");
                statement = connection.createStatement();
                ResultSet productionResultSet = statement.executeQuery("SELECT * FROM products WHERE code = " + productCode);
                if (productionResultSet.next()) {
                    Product product = new Product(productionResultSet.getLong("code"), productionResultSet.getString("name"));
                    return new ReceiptItem(resultSet.getLong("id"), resultSet.getLong("receipt_id"), product, resultSet.getInt("price"), resultSet.getInt("amount"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        throw new IllegalStateException("Receipt item with id " + pk + " not found");

    }

    @Override
    public List<ReceiptItem> getAll() {
        List<ReceiptItem> result = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM receipt_items");
            while (resultSet.next()) {
                Long productCode = resultSet.getLong("product_code");
                statement = connection.createStatement();
                ResultSet productionResultSet = statement.executeQuery("SELECT * FROM products WHERE code = " + productCode);
                productionResultSet.next();
                Product product = new Product(productionResultSet.getLong("code"), productionResultSet.getString("name"));
                result.add(new ReceiptItem(resultSet.getLong("id"), resultSet.getLong("receipt_id"), product, resultSet.getInt("price"), resultSet.getInt("amount")));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void delete(Long pk) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM receipt_items WHERE id = ?")) {
            preparedStatement.setLong(1, pk);
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalStateException("Receipt with id = " + pk + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(ReceiptItem object) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE receipt_items SET id = ?, receipt_id = ?, product_code = ?, price = ?, amount = ? WHERE id = ?")) {
            int fieldIndex = 1;
            preparedStatement.setLong(fieldIndex++, object.getId());
            preparedStatement.setLong(fieldIndex++, object.getReceiptId());
            preparedStatement.setLong(fieldIndex++, object.getProduct().getCode());
            preparedStatement.setLong(fieldIndex++, object.getPrice());
            preparedStatement.setLong(fieldIndex++, object.getAmount());
            preparedStatement.setLong(fieldIndex, object.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(ReceiptItem object) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO receipt_items (receipt_id, product_code, price, amount) VALUES(?,?,?,?)")) {
            int fieldIndex = 1;
            preparedStatement.setLong(fieldIndex++, object.getReceiptId());
            preparedStatement.setLong(fieldIndex++, object.getProduct().getCode());
            preparedStatement.setLong(fieldIndex++, object.getPrice());
            preparedStatement.setLong(fieldIndex, object.getAmount());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
