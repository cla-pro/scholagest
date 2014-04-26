package net.scholagest.tester.utils;

import java.util.List;

import net.scholagest.test.util.H2Database;
import net.scholagest.test.util.TransactionalHelper;
import net.scholagest.utils.PersistInitializer;

import org.eclipse.jetty.client.api.ContentResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;

public abstract class AbstractTestSuite {

    private static H2Database h2Database;
    private static JettyServer jettyServer;

    private JettyClient jettyClient;

    private Injector injector;

    // Called before the implementations' @BeforeClass
    @BeforeClass
    public static void setUpClass() throws Exception {
        h2Database = new H2Database();

        jettyServer = new JettyServer();
        jettyServer.start();
    }

    // Called before the implementations' @Before
    @Before
    public void setUp() throws Exception {
        h2Database.start();

        jettyClient = new JettyClient();
        jettyClient.start();

        initializeContext();
    }

    private void initializeContext() {
        final Module[] modules = new Module[] { new JpaPersistModule("scholagest-test-pu"), new JpaInitializerGuiceContext() };
        injector = Guice.createInjector(modules);
        injector.getInstance(PersistInitializer.class);
    }

    // Called after the implementations' @After
    @After
    public void tearDown() throws Exception {
        jettyClient.stop();
        h2Database.stop();
    }

    // Called after the implementations' @AfterClass
    @AfterClass
    public static void tearDownClass() throws Exception {
        jettyServer.stop();
    }

    @SafeVarargs
    protected final <T> void persistInTransaction(final T... entities) {
        for (final T entity : entities) {
            final TransactionalHelper transcationHelper = injector.getInstance(TransactionalHelper.class);
            transcationHelper.persistEntity(entity);
        }
    }

    protected ContentResponse callGET(final String url, final List<UrlParameter> parameters) throws Exception {
        return jettyClient.callGET(url, parameters);
    }

    protected ContentResponse callPOST(final String url, final String content) throws Exception {
        return jettyClient.callPOST(url, content);
    }

    private class JpaInitializerGuiceContext extends AbstractModule {
        @Override
        protected void configure() {
            bind(TransactionalHelper.class);
            bind(PersistInitializer.class);
        }
    }
}
