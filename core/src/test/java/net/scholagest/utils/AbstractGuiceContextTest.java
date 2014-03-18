package net.scholagest.utils;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.scholagest.authorization.AuthorizationInterceptor;
import net.scholagest.authorization.RolesAndPermissions;

import org.apache.shiro.subject.Subject;
import org.junit.Before;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.matcher.Matchers;

public abstract class AbstractGuiceContextTest {
    private final AuthorizationInterceptor authorizationInterceptor = new AuthorizationInterceptor();

    private Injector injector;

    @Before
    public void setUpContext() {
        injector = Guice.createInjector(new TestGuiceContext());
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

            configureContext(this);
        }

        @Override
        public <T> AnnotatedBindingBuilder<T> bind(final Class<T> clazz) {
            return super.bind(clazz);
        }
    }
}
