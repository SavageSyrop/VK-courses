package ru.vk.dao;

import org.jetbrains.annotations.NotNull;
import ru.vk.entities.Organisation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class OrganisationDAO extends AbstractDAO<Organisation> {
    public OrganisationDAO(@NotNull Connection connection) {
        super(connection);
    }

    @Override
    public Organisation get(Long pk) {
        try (Statement statement = getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM organisations WHERE tax_number = " + pk);
            if (resultSet.next()) {
                return new Organisation(resultSet.getLong("tax_number"), resultSet.getString("name"), resultSet.getLong("checking_account"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        throw new IllegalStateException("Organisation with tax number " + pk + " not found");
    }

    @Override
    public List<Organisation> getAll() {
        List<Organisation> result = new ArrayList<>();
        try (Statement statement = getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM organisations");
            while (resultSet.next()) {
                result.add(new Organisation(resultSet.getLong("tax_number"), resultSet.getString("name"), resultSet.getLong("checking_account")));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void delete(Long pk) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("DELETE FROM organisations WHERE tax_number = ?")) {
            preparedStatement.setLong(1, pk);
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalStateException("Organisation with id = " + pk + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Organisation object) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE organisations SET tax_number = ?, name = ?, checking_account = ? WHERE tax_number = ?")) {
            int fieldIndex = 1;
            preparedStatement.setLong(fieldIndex++, object.getTaxNumber());
            preparedStatement.setString(fieldIndex++, object.getName());
            preparedStatement.setLong(fieldIndex, object.getCheckingAccount());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(Organisation object) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO organisations(tax_number, name, checking_account) VALUES(?,?,?)")) {
            int fieldIndex = 1;
            preparedStatement.setLong(fieldIndex++, object.getTaxNumber());
            preparedStatement.setString(fieldIndex++, object.getName());
            preparedStatement.setLong(fieldIndex, object.getCheckingAccount());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Organisation> getTopTenOrganisationsByAmount() {
        List<Organisation> topOrganisations = new ArrayList<>();
        try (Statement statement = getConnection().createStatement()) {
            String query = "SELECT o.* FROM organisations AS o INNER JOIN receipts AS r ON r.organisation_tax_number = o.tax_number INNER JOIN receipt_items AS i ON r.id = i.receipt_id GROUP BY o.tax_number ORDER BY sum(i.amount) DESC LIMIT 10";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                topOrganisations.add(new Organisation(resultSet.getLong("tax_number"), resultSet.getString("name"), resultSet.getLong("checking_account")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topOrganisations;
    }


    public List<Organisation> getOrganisationsWithByAmountMoreThanParameter(Integer productCode, Integer limit) {
        List<Organisation> organisations = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT o.* FROM organisations AS o INNER JOIN receipts AS r ON r.organisation_tax_number = o.tax_number INNER JOIN receipt_items AS i ON r.id = i.receipt_id GROUP BY o.tax_number, i.product_code HAVING sum(i.amount) > ? and i.product_code = ?")) {
            statement.setLong(1, limit);
            statement.setLong(2, productCode);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                organisations.add(new Organisation(resultSet.getLong("tax_number"), resultSet.getString("name"), resultSet.getLong("checking_account")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return organisations;
    }
}
