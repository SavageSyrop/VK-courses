package ru.vk;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.JDBCLoginService;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.Properties;

import static org.eclipse.jetty.server.CustomRequestLog.DEFAULT_DATE_FORMAT;
import static org.eclipse.jetty.server.CustomRequestLog.NCSA_FORMAT;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        FlywayInitializer.initDatabase();

        Properties properties = new Properties();
        properties.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        properties.setProperty("dataSource.user", DatabaseCredentials.LOGIN.getValue());
        properties.setProperty("dataSource.password", DatabaseCredentials.PASSWORD.getValue());
        properties.setProperty("dataSource.databaseName", DatabaseCredentials.DATABASE_NAME.getValue());
        HikariConfig config = new HikariConfig(properties);
        Connection connection;
        HikariDataSource dataSource = new HikariDataSource(config);
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        HttpConfiguration configuration = new HttpConfiguration();
        Server server = new Server();
        HttpConnectionFactory connectionFactory = new HttpConnectionFactory(configuration);
        ServerConnector serverConnector = new ServerConnector(server, connectionFactory);

        serverConnector.setHost("localhost");
        serverConnector.setPort(3466);
        server.setConnectors(new Connector[]{serverConnector});

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");

        context.setWelcomeFiles(new String[]{"/static/mainpage.html"});
        context.addServlet(new ServletHolder("default", DefaultServlet.class), "/");
        context.addServlet(new ServletHolder("products", new MainServlet(new ProductDAO(connection), new CompanyDAO(connection))), "/products");

        Path staticResources = Paths.get(Main.class.getResource("/static").toURI());

        final AsyncRequestLogWriter writer = new AsyncRequestLogWriter(
                staticResources.toString() + '/' + "app.log"
        );
        context.setBaseResource(Resource.newResource(staticResources));

        writer.setAppend(true);
        writer.setRetainDays(7);
        String format = NCSA_FORMAT.replace("%t", "%{" + DEFAULT_DATE_FORMAT + '|' + "}t");
        CustomRequestLog customRequestLog = new CustomRequestLog(writer, format);
        server.setRequestLog(customRequestLog);


        Filter filter = new MainFilter();
        context.addFilter(new FilterHolder(filter), "/products", EnumSet.of(DispatcherType.REQUEST));

        String jbdcConfig = Main.class.getResource("/jdbc_config").toExternalForm();
        JDBCLoginService loginService = new JDBCLoginService("login", jbdcConfig);
        ConstraintSecurityHandler securityHandler = new SecurityHandlerBuilder().build(loginService);
        context.setSecurityHandler(securityHandler);
        server.addBean(loginService);
        server.setHandler(context);
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}