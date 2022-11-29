package ru.vk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.vk.dao.OrganisationDAO;
import ru.vk.dao.ProductDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        FlywayInitializer.initDatabase();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Connection connection = DriverManager.getConnection(JDBCCredentials.URL.getValue() + JDBCCredentials.DATABASE_NAME.getValue(), JDBCCredentials.LOGIN.getValue(), JDBCCredentials.PASSWORD.getValue())) {
            OrganisationDAO organisationDAO = new OrganisationDAO(connection);
            ProductDAO productDAO = new ProductDAO(connection);
            LocalDate from = LocalDate.of(2022, 11, 11);
            LocalDate to = LocalDate.of(2022, 11, 21);
            System.out.println(gson.toJson(organisationDAO.getTopTenOrganisationsByAmount()));
            System.out.println(gson.toJson(organisationDAO.getOrganisationsWithByAmountMoreThanParameter(1, 90)));
            System.out.println(gson.toJson(productDAO.getEveryDayProductStatsBetweenDates(from, to)));
            System.out.println(gson.toJson(productDAO.getAveragePriceOfEveryProductBetweenDates(from, to)));
            System.out.println(gson.toJson(productDAO.getAllProductsBetweenDates(from, to)));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}