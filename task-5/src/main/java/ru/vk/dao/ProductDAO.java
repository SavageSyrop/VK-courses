package ru.vk.dao;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import ru.vk.entities.Product;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends AbstractDAO<Product> {
    public ProductDAO(@NotNull Connection connection) {
        super(connection);
    }

    @Override
    public final Product get(Long pk) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM products WHERE code = " + pk);
            if (resultSet.next()) {
                return new Product(resultSet.getLong("code"), resultSet.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        throw new IllegalStateException("Product with tax number " + pk + " not found");
    }

    @Override
    public List<Product> getAll() {
        List<Product> result = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM products");
            while (resultSet.next()) {
                result.add(new Product(resultSet.getLong("code"), resultSet.getString("name")));
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void delete(Long pk) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM products WHERE code = ?")) {
            preparedStatement.setLong(1, pk);
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalStateException("Product with id = " + pk + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Product object) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE products SET code = ?, name = ? WHERE code = ?")) {
            int fieldIndex = 1;
            preparedStatement.setLong(fieldIndex++, object.getCode());
            preparedStatement.setString(fieldIndex++, object.getName());
            preparedStatement.setLong(fieldIndex, object.getCode());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(Product object) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO products(name) VALUES(?)")) {
            int fieldIndex = 1;
            preparedStatement.setString(fieldIndex, object.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<JsonObject> getEveryDayProductStatsBetweenDates(LocalDate from, LocalDate to) {
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("Wrong dates: period end is before period start");
        }
        List<JsonObject> results = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT r.creation_date, i.product_code, sum(i.amount) AS total_amount, sum(i.price) AS price, sum(i.amount * i.price) AS total_price from receipt_items AS i LEFT JOIN receipts AS r ON i.receipt_id = r.id WHERE r.creation_date BETWEEN ? AND ? GROUP BY r.creation_date, i.product_code ORDER BY r.creation_date")) {
            preparedStatement.setDate(1, Date.valueOf(from));
            preparedStatement.setDate(2, Date.valueOf(to));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("creationDate", String.valueOf(resultSet.getDate("creation_date")));
                jsonObject.addProperty("productCode", resultSet.getLong("product_code"));
                results.add(jsonObject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<JsonObject> getAveragePriceOfEveryProductBetweenDates(LocalDate from, LocalDate to) {
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("Wrong dates: period end is before period start");
        }
        List<JsonObject> results = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT i.product_code, avg(price) AS average_price FROM receipt_items AS i LEFT JOIN receipts AS r ON i.receipt_id = r.id WHERE r.creation_date BETWEEN ? AND ? GROUP BY i.product_code")) {
            preparedStatement.setDate(1, Date.valueOf(from));
            preparedStatement.setDate(2, Date.valueOf(to));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("productCode", resultSet.getLong("product_code"));
                jsonObject.addProperty("averagePrice", resultSet.getDouble("average_price"));
                results.add(jsonObject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<JsonObject> getAllProductsBetweenDates(LocalDate from, LocalDate to) {
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("Wrong dates: period end is before period start");
        }
        List<JsonObject> results = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT product_code, p.name as product_name , o.name AS organisation_name FROM organisations AS o LEFT JOIN receipts as r ON r.organisation_tax_number = o.tax_number LEFT JOIN receipt_items AS i ON i.receipt_id = r.id LEFT JOIN products AS p ON i.product_code = p.code WHERE (creation_date  is NULL) OR (creation_date>=? AND creation_date<=?)")) {
            preparedStatement.setDate(1, Date.valueOf(from));
            preparedStatement.setDate(2, Date.valueOf(to));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("productCode", resultSet.getLong("product_code"));
                jsonObject.addProperty("productName", resultSet.getString("product_name"));
                jsonObject.addProperty("organisationName", resultSet.getString("organisation_name"));
                results.add(jsonObject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
