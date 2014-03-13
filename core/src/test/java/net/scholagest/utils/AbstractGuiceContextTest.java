package net.scholagest.utils;

import net.scholagest.authorization.AuthorizationInterceptor;
import net.scholagest.authorization.RolesAndPermissions;

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
