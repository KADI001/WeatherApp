package org.kadirov;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.kadirov.config.Environment;
import org.kadirov.db.LiquibaseService;
import org.kadirov.entity.location.Location;
import org.kadirov.entity.session.Session;
import org.kadirov.entity.user.User;
import org.testcontainers.containers.MySQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

@Slf4j
public abstract class TestContainersTest extends IntegrationTest {

    public static final MySQLContainer<?> dbContainer =
            new MySQLContainer<>("mysql:8.0-oracle")
                    .withDatabaseName("weather_app")
                    .withUsername(Environment.getProperty("hibernate.connection.username"))
                    .withPassword(Environment.getProperty("hibernate.connection.password"));

    public static final SessionFactory sessionFactory;

    static {
        try {
            dbContainer.setPortBindings(List.of("3312:3306"));
            dbContainer.start();

            log.debug("Connecting to {}", Environment.getProperty("hibernate.connection.url"));

            sessionFactory = new Configuration()
                    .addProperties(Environment.getPropertiesByStartsWith("hibernate."))
                    .addAnnotatedClass(Session.class)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Location.class)
                    .buildSessionFactory();

            Connection connection = DriverManager.getConnection(
                    Environment.getProperty("hibernate.connection.url"),
                    Environment.getProperty("hibernate.connection.username"),
                    Environment.getProperty("hibernate.connection.password")
            );

            String changeLog = Environment.getProperty("liquibase.changelog");
            LiquibaseService liquibaseService = new LiquibaseService(changeLog, connection);

            liquibaseService.applyMigrations();
        } catch (Exception e) {
            log.error("Error during init TestContainers", e);
            throw new RuntimeException(e);
        }
    }
}
