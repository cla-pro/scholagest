package net.scholagest.tester.utils;

import java.util.List;
import java.util.UUID;

import net.scholagest.db.entity.SessionEntity;
import net.scholagest.db.entity.TeacherDetailEntity;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.db.entity.UserEntity;
import net.scholagest.test.util.H2Database;
import net.scholagest.test.util.TransactionalHelper;
import net.scholagest.tester.utils.creator.SessionEntityCreator;
import net.scholagest.tester.utils.creator.TeacherEntityCreator;
import net.scholagest.tester.utils.creator.UserEntityCreator;
import net.scholagest.utils.PersistInitializer;

import org.eclipse.jetty.client.api.ContentResponse;
import org.joda.time.DateTime;
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

    protected String createAndGetSessionTokenForAdmin() {
        final String sessionId = UUID.randomUUID().toString();

        final TeacherEntity teacherEntity = TeacherEntityCreator.createTeacherEntity("Barack", "Avendre", null);
        final UserEntity userEntity = UserEntityCreator.createUserEntity("bavendre", "", "ADMIN", teacherEntity);
        final SessionEntity sessionEntity = SessionEntityCreator.createSessionEntity(sessionId, new DateTime().plusHours(2), userEntity);

        persistInTransaction(teacherEntity, userEntity, sessionEntity);

        final TeacherDetailEntity teacherDetailEntity = TeacherEntityCreator.createTeacherDetailEntity(null, null, null, teacherEntity);
        persistInTransaction(teacherDetailEntity);

        return sessionId;
    }

    @SafeVarargs
    protected final <T> void persistInTransaction(final T... entities) {
        final TransactionalHelper transcationHelper = getTransactionalHelper();
        for (final T entity : entities) {
            transcationHelper.persistEntity(entity);
        }
    }

    protected TransactionalHelper getTransactionalHelper() {
        return injector.getInstance(TransactionalHelper.class);
    }

    protected ContentResponse callGET(final String url, final List<UrlParameter> parameters, final String token) throws Exception {
        return jettyClient.callGET(url, parameters, token);
    }

    protected ContentResponse callPOST(final String url, final String content, final String token) throws Exception {
        return jettyClient.callPOST(url, content, token);
    }

    protected ContentResponse callPUT(final String url, final String content, final String token) throws Exception {
        return jettyClient.callPUT(url, content, token);
    }

    private class JpaInitializerGuiceContext extends AbstractModule {
        @Override
        protected void configure() {
            bind(TransactionalHelper.class);
            bind(PersistInitializer.class);
        }
    }
}
