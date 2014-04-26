package net.scholagest.utils;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import liquibase.exception.LiquibaseException;
import net.scholagest.authorization.AuthorizationInterceptor;
import net.scholagest.authorization.RolesAndPermissions;
import net.scholagest.test.util.H2Database;
import net.scholagest.test.util.TransactionalHelper;

import org.apache.shiro.subject.Subject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.matcher.Matchers;
import com.google.inject.persist.jpa.JpaPersistModule;

public abstract class AbstractGuiceContextTest {
    private final AuthorizationInterceptor authorizationInterceptor = new AuthorizationInterceptor();

    private static H2Database h2database;

    private final boolean useH2Database;
    private Injector injector;

    protected AbstractGuiceContextTest() {
        this(false);
    }

    protected AbstractGuiceContextTest(final boolean useH2Database) {
        this.useH2Database = useH2Database;
    }

    @BeforeClass
    public static void setUpClass() throws SQLException {
        h2database = new H2Database();
    }

    @Before
    public void setUpContext() throws ClassNotFoundException, LiquibaseException, SQLException {
        if (useH2Database) {
            h2database.start();
        }

        final Module[] modules;
        if (useH2Database) {
            modules = new Module[] { new JpaPersistModule("scholagest-test-pu"), new JpaInitializerGuiceContext(), new TestGuiceContext() };
        } else {
            modules = new Module[] { new TestGuiceContext() };
        }

        injector = Guice.createInjector(modules);

        if (useH2Database) {
            injector.getInstance(PersistInitializer.class);
        }
    }

    @After
    public void tearDownContext() {
        h2database.stop();
    }

    @AfterClass
    public static void tearDownClass() {

    }

    protected void executeInTransaction(final Runnable runnable) {
        getInstance(TransactionalHelper.class).executeInTransaction(runnable);
    }

    protected <T> T getInstance(final Class<T> clazz) {
        return injector.getInstance(clazz);
    }

    protected abstract void configureContext(TestGuiceContext module);

    protected void setAdminSubject() {
        final Subject subject = mock(Subject.class);
        when(subject.hasRole(anyString())).thenReturn(true);
        when(subject.isPermitted(anyString())).thenReturn(true);

        ScholagestThreadLocal.setSubject(subject);
    }

    protected void setNoRightSubject() {
        final Subject subject = mock(Subject.class);
        when(subject.hasRole(anyString())).thenReturn(false);
        when(subject.isPermitted(anyString())).thenReturn(false);

        ScholagestThreadLocal.setSubject(subject);
    }

    protected class TestGuiceContext extends AbstractModule {
        @Override
        protected void configure() {
            requestInjection(authorizationInterceptor);
            bindInterceptor(Matchers.any(), Matchers.annotatedWith(RolesAndPermissions.class), authorizationInterceptor);

            bind(TransactionalHelper.class);

            configureContext(this);
        }

        // Do not remove because it is used inside the abstract method
        @Override
        public <T> AnnotatedBindingBuilder<T> bind(final Class<T> clazz) {
            return super.bind(clazz);
        }

        // Do not remove because it is used inside the abstract method
        @Override
        public void requestInjection(final Object instance) {
            super.requestInjection(instance);
        }
    }

    private class JpaInitializerGuiceContext extends AbstractModule {
        @Override
        protected void configure() {
            bind(PersistInitializer.class);
        }
    }
}
