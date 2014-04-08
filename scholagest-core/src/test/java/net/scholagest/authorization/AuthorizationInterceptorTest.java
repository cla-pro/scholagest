package net.scholagest.authorization;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.shiro.subject.Subject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;

public class AuthorizationInterceptorTest {
    private AuthorizationInterceptor authorizationInterceptor;

    private InterceptedClass interceptedClass;

    private Subject subject;

    @Before
    public void setUp() {
        authorizationInterceptor = new AuthorizationInterceptor();
        final Injector injector = Guice.createInjector(new GuiceContext());
        interceptedClass = injector.getInstance(InterceptedClass.class);

        subject = mock(Subject.class);
        when(subject.hasRole(anyString())).thenReturn(false);
        when(subject.isPermitted(anyString())).thenReturn(false);
        ScholagestThreadLocal.setSubject(subject);
    }

    @After
    public void tearDown() {
        ScholagestThreadLocal.setSubject(null);
    }

    @Test
    public void testRoleSucceed() {
        when(subject.hasRole(eq("ADMIN"))).thenReturn(true);

        interceptedClass.testOnlyRole();
    }

    @Test(expected = ScholagestRuntimeException.class)
    public void testRoleFail() {
        interceptedClass.testOnlyRole();
    }

    @Test
    public void testRoleOrPermissionSucceedRole() {
        when(subject.hasRole(eq("ADMIN"))).thenReturn(true);

        interceptedClass.testRoleOrPermission(null, null);
    }

    @Test
    public void testRoleOrPermissionSucceedPermission() {
        when(subject.isPermitted(eq("permission"))).thenReturn(true);

        interceptedClass.testRoleOrPermission(null, "permission");
    }

    @Test(expected = ScholagestRuntimeException.class)
    public void testRoleOrPermissionFail() {
        interceptedClass.testRoleOrPermission(null, null);
    }

    @Test(expected = ScholagestRuntimeException.class)
    public void testInvalidPermission() {
        interceptedClass.testInvalidPermission(new Object());
    }

    @Test
    public void testNoRoleOrPermissionSucceed() {
        interceptedClass.testNoRoleAndPermission();
    }

    @Test(expected = ScholagestRuntimeException.class)
    public void testNoSubject() {
        ScholagestThreadLocal.setSubject(null);
        interceptedClass.testOnlyRole();
    }

    public static class InterceptedClass {
        @Inject
        public InterceptedClass() {}

        @RolesAndPermissions(roles = { "ADMIN" })
        public int testOnlyRole() {
            return 0;
        }

        @RolesAndPermissions(roles = { "ADMIN" })
        public int testRoleOrPermission(final Object test, @Permission final String permission) {
            return 1;
        }

        @RolesAndPermissions(roles = {})
        public int testInvalidPermission(final @Permission Object test) {
            return 1;
        }

        @RolesAndPermissions(roles = {})
        public int testNoRoleAndPermission() {
            return 2;
        }
    }

    public class GuiceContext extends AbstractModule {
        @Override
        protected void configure() {
            requestInjection(authorizationInterceptor);
            bindInterceptor(Matchers.any(), Matchers.annotatedWith(RolesAndPermissions.class), authorizationInterceptor);

            bind(InterceptedClass.class);
        }
    }
}
