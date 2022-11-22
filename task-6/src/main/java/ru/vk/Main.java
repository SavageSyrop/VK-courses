package ru.vk;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.vk.dao.OrganisationDAO;
import ru.vk.dao.ProductDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;


public class Main {
    public static void main(String[] args) {
        FlywayInitializer.initDatabase();
        Properties properties = new Properties();
        properties.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        properties.setProperty("dataSource.user", DatabaseCredentials.LOGIN.getValue());
        properties.setProperty("dataSource.password", DatabaseCredentials.PASSWORD.getValue());
        properties.setProperty("dataSource.databaseName", DatabaseCredentials.DATABASE_NAME.getValue());
        HikariConfig config = new HikariConfig(properties);
        try (HikariDataSource dataSource = new HikariDataSource(config)) {
            try (Connection connection = dataSource.getConnection()) {
                OrganisationDAO organisationDAO = new OrganisationDAO(connection);
                ProductDAO productDAO = new ProductDAO(connection);
                LocalDate from = LocalDate.of(2022, 11, 11);
                LocalDate to = LocalDate.of(2022, 11, 21);
                organisationDAO.getTopTenOrganisationsByAmount().forEach(System.out::println);
                System.out.println("===============================================================");
                organisationDAO.getOrganisationsWithByAmountMoreThanParameter(1, 10).forEach(System.out::println);
                System.out.println("===============================================================");
                productDAO.getAllProductsBetweenDates(from, to).forEach(System.out::println);
                System.out.println("===============================================================");
                productDAO.getAveragePriceOfEveryProductBetweenDates(from, to).forEach(System.out::println);
                System.out.println("===============================================================");
                productDAO.getEveryDayProductStatsBetweenDates(from, to).forEach(System.out::println);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}