package ru.vk.rest;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import ru.vk.CompanyDAO;
import ru.vk.ProductDAO;
import ru.vk.database.DatabaseConnector;
import ru.vk.database.DatabaseCredentials;

import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.List;

public class GuiceInjector extends GuiceResteasyBootstrapServletContextListener {

    @Override
    protected List<? extends Module> getModules(ServletContext context) {
        return Collections.singletonList(new GuiceModule());
    }

    private static final class GuiceModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(DSLContext.class).toInstance(DSL.using(DatabaseConnector.getConnection(), SQLDialect.valueOf(DatabaseCredentials.SQL_DIALECT.getValue())));
            bind(JacksonMessageBodyHandler.class).toInstance(new JacksonMessageBodyHandler());
            bind(ObjectMapperProvider.class).toInstance(new ObjectMapperProvider());
            bind(CompanyDAO.class);
            bind(ProductDAO.class);
            bind(ProductsREST.class);
            bind(StaticPage.class);
        }
    }
}
