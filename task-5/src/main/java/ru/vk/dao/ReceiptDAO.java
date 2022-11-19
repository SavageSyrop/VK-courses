package ru.vk.dao;

import org.jetbrains.annotations.NotNull;
import ru.vk.entities.Organisation;
import ru.vk.entities.Product;
import ru.vk.entities.Receipt;
import ru.vk.entities.ReceiptItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class ReceiptDAO extends AbstractDAO<Receipt> {
    public ReceiptDAO(@NotNull Connection connection) {
        super(connection);
    }

    @Override
    public Receipt get(Long pk) {
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM receipts WHERE id = " + pk);
            if (resultSet.next()) {
                Long organisationPrimaryKey = resultSet.getLong("organisation_tax_number");
                statement = getConnection().createStatement();
                ResultSet organisationResultSet = statement.executeQuery("SELECT * FROM organisations WHERE tax_number = " + organisationPrimaryKey);
                organisationResultSet.next();
                Organisation organisation = new Organisation(organisationResultSet.getLong("tax_number"), organisationResultSet.getString("name"), organisationResultSet.getLong("checking_account"));
                statement = getConnection().createStatement();
                ResultSet itemsResultSet = statement.executeQuery("SELECT * FROM receipt_items WHERE receipt_id = " + resultSet.getLong("id"));
                List<ReceiptItem> items = new ArrayList<>();
                while (itemsResultSet.next()) {
                    Long productCode = itemsResultSet.getLong("product_code");
                    statement = getConnection().createStatement();
                    ResultSet productionResultSet = statement.executeQuery("SELECT * FROM products WHERE code = " + productCode);
                    productionResultSet.next();
                    Product product = new Product(productionResultSet.getLong("code"), productionResultSet.getString("name"));
                    items.add(new ReceiptItem(itemsResultSet.getLong("id"), itemsResultSet.getLong("receipt_id"), product, itemsResultSet.getInt("price"), itemsResultSet.getInt("amount")));
                }
                return new Receipt(resultSet.getLong("id"), resultSet.getDate("creation_date").toLocalDate(), organisation, items);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        throw new IllegalStateException("Receipt with tax number " + pk + " not found");
    }

    @Override
    public List<Receipt> getAll() {
        List<Receipt> result = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM receipts");
            while (resultSet.next()) {
                Long organisationPrimaryKey = resultSet.getLong("organisation_tax_number");
                statement = getConnection().createStatement();
                ResultSet organisationResultSet = statement.executeQuery("SELECT * FROM organisations WHERE tax_number = " + organisationPrimaryKey);
                organisationResultSet.next();
                Organisation organisation = new Organisation(organisationResultSet.getLong("tax_number"), organisationResultSet.getString("name"), organisationResultSet.getLong("checking_account"));
                statement = getConnection().createStatement();
                ResultSet itemsResultSet = statement.executeQuery("SELECT * FROM receipt_items WHERE receipt_id = " + resultSet.getLong("id"));
                List<ReceiptItem> items = new ArrayList<>();
                while (itemsResultSet.next()) {
                    Long productCode = itemsResultSet.getLong("product_code");
                    statement = getConnection().createStatement();
                    ResultSet productionResultSet = statement.executeQuery("SELECT * FROM products WHERE code = " + productCode);
                    productionResultSet.next();
                    Product product = new Product(productionResultSet.getLong("code"), productionResultSet.getString("name"));
                    items.add(new ReceiptItem(itemsResultSet.getLong("id"), itemsResultSet.getLong("receipt_id"), product, itemsResultSet.getInt("price"), itemsResultSet.getInt("amount")));
                }
                result.add(new Receipt(resultSet.getLong("id"), resultSet.getDate("creation_date").toLocalDate(), organisation, items));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void delete(Long pk) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("DELETE FROM receipts WHERE id = ?")) {
            preparedStatement.setLong(1, pk);
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalStateException("Receipt with id = " + pk + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Receipt object) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE receipts SET id = ?, creation_date = ?, organisation_tax_number = ? WHERE id = ?")) {
            int fieldIndex = 1;
            preparedStatement.setLong(fieldIndex++, object.getId());
            preparedStatement.setDate(fieldIndex++, Date.valueOf(object.getCreationDate()));
            preparedStatement.setLong(fieldIndex, object.getOrganisation().getTaxNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(Receipt object) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO receipts(creation_date, organisation_tax_number) VALUES(?,?)")) {
            int fieldIndex = 1;
            preparedStatement.setDate(fieldIndex++, Date.valueOf(object.getCreationDate()));
            preparedStatement.setLong(fieldIndex, object.getOrganisation().getTaxNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
