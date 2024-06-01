package org.kadirov.db;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;

public class LiquibaseService implements MigrationService {

    private final Liquibase liquibase;

    public LiquibaseService(String changeLogFilePath, Connection connection) {
        try {
            liquibase = new Liquibase(changeLogFilePath,
                    new ClassLoaderResourceAccessor(getClass().getClassLoader()),
                    new JdbcConnection(connection));
        } catch (LiquibaseException le) {
            throw new MigrationException("Failed to initiate Liquibase", le);
        }
    }

    @Override
    public void applyMigrations() {
        try {
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (LiquibaseException le) {
            throw new MigrationException("Failed to apply Liquibase migrations", le);
        }
    }
}
