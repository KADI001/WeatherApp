package org.kadirov;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.kadirov.config.Environment;
import org.kadirov.config.TemplatedFilePropertiesSupplier;
import org.kadirov.dao.EntityDAOFactory;
import org.kadirov.db.LiquibaseService;
import org.kadirov.dao.location.LocationDAOImpl;
import org.kadirov.entity.location.Location;
import org.kadirov.service.WeatherServiceImpl;
import org.kadirov.service.auth.AuthService;
import org.kadirov.service.auth.AuthServiceImpl;
import org.kadirov.service.integration.openweatherapi.OpenWeatherMapServiceImpl;
import org.kadirov.service.session.ExpiredSessionsCollector;
import org.kadirov.service.session.ExpiredSessionsCollectorImpl;
import org.kadirov.dao.session.SessionDAO;
import org.kadirov.dao.session.SessionDAOImpl;
import org.kadirov.entity.session.Session;
import org.kadirov.dao.user.UserDAO;
import org.kadirov.dao.user.UserDAOImpl;
import org.kadirov.entity.user.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.temporal.ChronoUnit;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.parseInt;
import static org.kadirov.util.EnvPropUtils.getEnvProp;

@Slf4j
@WebListener
public class StartupListener implements ServletContextListener {

    public static final String HIBERNATE_XML_CONFIG = "HIBERNATE_XML_CONFIG";
    private static final String APPLICATION_CONFIG = "APPLICATION_CONFIG";

    private LocationDAOImpl locationDAOImpl;
    private SessionDAO sessionDAO;
    private UserDAO userDAO;
    private ExpiredSessionsCollector expiredSessionsCollectorImpl;
    private EntityDAOFactory entityDAOFactory;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ServletContext servletContext = sce.getServletContext();

            setupPropertiesSources();

            applyingMigrations();

            setupThymeleaf(servletContext);
            setupDAOs();
            setupServices(servletContext);

            startBackgroundProcesses();
        } catch (Exception e) {
            log.error("Startup error occurred.", e);
            throw new RuntimeException(e);
        }
    }

    private void startBackgroundProcesses() {
        log.debug("Starts background processes");

        int cleanInterval = parseInt(Environment.getProperty("session.expired.clean.interval"));
        expiredSessionsCollectorImpl = new ExpiredSessionsCollectorImpl(sessionDAO, cleanInterval, TimeUnit.HOURS);

        if (!expiredSessionsCollectorImpl.start()) {
            log.error("Failed to start background process 'expiredSessionsCollector'");
        }
    }

    private void setupServices(ServletContext servletContext) {
        log.debug("Setup Services");

        String baseUrl = Environment.getProperty("integration.openweathermap.baseUrl");
        String apiKey = Environment.getProperty("integration.openweathermap.apiKey");
        OpenWeatherMapServiceImpl openWeatherMapService = new OpenWeatherMapServiceImpl(baseUrl, apiKey);
        WeatherServiceImpl weatherService = new WeatherServiceImpl(openWeatherMapService, locationDAOImpl);


        int sessionTTL = parseInt(Environment.getProperty("session.ttl.duration"));
        ChronoUnit sessionTTLUnit = ChronoUnit.valueOf(Environment.getProperty("session.ttl.unit"));
        AuthService authService = new AuthServiceImpl(sessionDAO, userDAO, sessionTTL, sessionTTLUnit);

        servletContext.setAttribute("locationDAO", locationDAOImpl);
        servletContext.setAttribute("sessionDAO", sessionDAO);
        servletContext.setAttribute("userDAO", userDAO);
        servletContext.setAttribute("openWeatherMapService", openWeatherMapService);
        servletContext.setAttribute("weatherService", weatherService);
        servletContext.setAttribute("authService", authService);
    }

    private void applyingMigrations() {
        log.debug("Applying migrations");

        String changelogFile = Environment.getProperty("liquibase.changelog");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(
                    Environment.getProperty("hibernate.connection.url"),
                    Environment.getProperty("hibernate.connection.username"),
                    Environment.getProperty("hibernate.connection.password")
            );

            LiquibaseService migrationService = new LiquibaseService(changelogFile, connection);
            migrationService.applyMigrations();
        } catch (Exception e) {
            throw new RuntimeException("Failed to apply migrations", e);
        }
    }

    private void setupDAOs() {
        log.debug("Setup Hibernate");

        SessionFactory sessionFactory = createSessionFactory();

        entityDAOFactory = new EntityDAOFactory(sessionFactory);

        locationDAOImpl = entityDAOFactory.createInstanceOf(LocationDAOImpl.class);
        sessionDAO = entityDAOFactory.createInstanceOf(SessionDAOImpl.class);
        userDAO = entityDAOFactory.createInstanceOf(UserDAOImpl.class);
    }

    private static void setupThymeleaf(ServletContext servletContext) {
        log.debug("Setup Thymeleaf");

        IWebApplication webApplication = JakartaServletWebApplication.buildApplication(servletContext);
        WebApplicationTemplateResolver webApplicationTemplateResolver = getWebApplicationTemplateResolver(webApplication);
        TemplateEngine templateEngine = getTemplateEngine(webApplicationTemplateResolver);

        servletContext.setAttribute("templateEngine", templateEngine);
        servletContext.setAttribute("webApplication", webApplication);
    }

    private static void setupPropertiesSources() {
        final String appConfig = getEnvProp(APPLICATION_CONFIG);

        if (appConfig == null)
            throw new RuntimeException("Failed to find application config file");

        Environment.registerPropertiesSupplier(new TemplatedFilePropertiesSupplier(appConfig));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        entityDAOFactory.close();

        if (!expiredSessionsCollectorImpl.finish()) {
            log.error("Failed to stop background process 'expiredSessionsCollector'");
        }
    }

    private static SessionFactory createSessionFactory() {
        final Configuration cfg = new Configuration()
                .addAnnotatedClass(Location.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Session.class);

        Properties hibernateProperties = Environment.getPropertiesByStartsWith("hibernate.");

        if (!hibernateProperties.isEmpty()) {
            cfg.addProperties(hibernateProperties);
        } else {
            final String xmlConfig = getEnvProp(HIBERNATE_XML_CONFIG);

            if (xmlConfig == null) {
                throw new RuntimeException("Couldn't define any config file for hibernate");
            }

            cfg.configure(xmlConfig);
        }

        return cfg.buildSessionFactory();
    }

    private static WebApplicationTemplateResolver getWebApplicationTemplateResolver(IWebApplication webApplication) {
        WebApplicationTemplateResolver webApplicationTemplateResolver = new WebApplicationTemplateResolver(webApplication);

        webApplicationTemplateResolver.setTemplateMode(TemplateMode.HTML);
        webApplicationTemplateResolver.setPrefix("/templates/");
        webApplicationTemplateResolver.setSuffix(".html");
        webApplicationTemplateResolver.setCharacterEncoding("UTF-8");

        webApplicationTemplateResolver.setCacheable(false);
        return webApplicationTemplateResolver;
    }

    private static TemplateEngine getTemplateEngine(WebApplicationTemplateResolver webApplicationTemplateResolver) {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(webApplicationTemplateResolver);
        return templateEngine;
    }
}
