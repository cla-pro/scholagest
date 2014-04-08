package net.scholagest.test.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import org.h2.tools.Server;

/**
 * Handle the H2 database for the test context
 * 
 * @author CLA
 * @since 0.15.0
 */
public class H2Database {
    private Server server = null;
    private Liquibase liquibase;

    /**
     * Start the H2 database
     * 
     * @throws SQLException H2 exception forwarded
     */
    public void start() throws SQLException {
        if (server == null) {
            server = Server.createTcpServer("-baseDir", null).start();
        } else if (!server.isRunning(false)) {
            server.start();
        } else {
            // TODO Log error.
        }
    }

    /**
     * Initialize the database schema. Everything is cleared away before the liquibase
     * part takes place to set up the schema.
     * 
     * @throws LiquibaseException Exception forwarded
     * @throws ClassNotFoundException Exception forwarded
     * @throws SQLException Exception forwarded
     */
    public void initialize() throws LiquibaseException, ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        final Connection connection = DriverManager.getConnection("jdbc:h2:mem:~test", "admin", "admin");

        final Statement statement = connection.createStatement();
        statement.execute("drop all objects");

        final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

        liquibase = new Liquibase("liquibase/database.xml", new ClassLoaderResourceAccessor(), database);
        liquibase.update((Contexts) null);

        database.close();
    }

    /**
     * Stop the H2 database
     */
    public void stop() {
        server.stop();
    }
}
