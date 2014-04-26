package net.scholagest.test.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 * Handle the H2 database for the test context
 * 
 * @author CLA
 * @since 0.15.0
 */
public class H2Database {

    /**
     * Start and initialize the database schema. Everything is cleared away before the liquibase
     * part takes place to set up the schema.
     * 
     * @throws LiquibaseException Exception forwarded
     * @throws ClassNotFoundException Exception forwarded
     * @throws SQLException Exception forwarded
     */
    public void start() throws LiquibaseException, ClassNotFoundException, SQLException {
        Connection connection = null;
        liquibase.database.core.H2Database databaseConnection = null;
        try {
            Class.forName("org.h2.Driver");

            // http://stackoverflow.com/questions/5763747/h2-in-memory-database-table-not-found
            // DB_CLOSE_DELAY is used to avoid the DB to be deleted when the
            // connection is closed.
            connection = DriverManager.getConnection("jdbc:h2:mem:scholagest;DB_CLOSE_DELAY=-1", "admin", "admin");
            connection.setAutoCommit(true);

            final JdbcConnection jdbcConnection = new JdbcConnection(connection);
            databaseConnection = new liquibase.database.core.H2Database();
            databaseConnection.setConnection(jdbcConnection);
            databaseConnection.setAutoCommit(true);

            final Statement statement = connection.createStatement();
            statement.execute("drop all objects");
            statement.close();

            final Liquibase liquibase = new Liquibase("liquibase/database.xml", new ClassLoaderResourceAccessor(), databaseConnection);
            liquibase.validate();
            liquibase.update((Contexts) null);

            databaseConnection.commit();
            connection.commit();
        } finally {
            if (databaseConnection != null) {
                databaseConnection.close();
            }

            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Stop the H2 database
     */
    public void stop() {
        // do nothing
    }
}
