package ru.vk;


import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.JDBCLoginService;
import org.eclipse.jetty.server.AsyncRequestLogWriter;
import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import ru.vk.database.FlywayInitializer;
import ru.vk.rest.GuiceInjector;
import ru.vk.server.JettyServer;
import ru.vk.server.SecurityHandlerBuilder;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.eclipse.jetty.server.CustomRequestLog.DEFAULT_DATE_FORMAT;
import static org.eclipse.jetty.server.CustomRequestLog.NCSA_FORMAT;


public final class RestEasyMain {

    public static void main(String[] args) throws Exception {
        FlywayInitializer.initDatabase();

        Server server = JettyServer.build();
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.addServlet(HttpServletDispatcher.class, "/");
        context.addEventListener(new GuiceInjector());

        Path staticResources = Paths.get(RestEasyMain.class.getResource("/static").toURI());
        final AsyncRequestLogWriter writer = new AsyncRequestLogWriter(
                staticResources.toString() + '/' + "app.log"
        );
        context.setBaseResource(Resource.newResource(staticResources));
        writer.setAppend(true);
        writer.setRetainDays(7);
        String format = NCSA_FORMAT.replace("%t", "%{" + DEFAULT_DATE_FORMAT + '|' + "}t");
        CustomRequestLog customRequestLog = new CustomRequestLog(writer, format);
        server.setRequestLog(customRequestLog);

        String jbdcConfig = RestEasyMain.class.getResource("/jdbc_config").toExternalForm();
        JDBCLoginService loginService = new JDBCLoginService("login", jbdcConfig);
        ConstraintSecurityHandler securityHandler = new SecurityHandlerBuilder().build(loginService);
        context.setSecurityHandler(securityHandler);
        server.addBean(loginService);

        server.setHandler(context);
        server.start();
    }
}

