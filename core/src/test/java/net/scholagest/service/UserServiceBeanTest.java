package net.scholagest.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import net.scholagest.business.UserBusinessLocal;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.object.User;
import net.scholagest.utils.AbstractGuiceContextTest;
import net.scholagest.utils.ScholagestThreadLocal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test class for {@see UserServiceBean}
 * 
 * @author CLA
 * @since 0.13.0
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceBeanTest extends AbstractGuiceContextTest {
    @Mock
    private UserBusinessLocal userBusiness;

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(UserBusinessLocal.class).toInstance(userBusiness);
        module.bind(UserServiceLocal.class).to(UserServiceBean.class);
    }

    @Test
    public void testGetUser() {
        setAdminSubject();
        final UserServiceLocal testee = getInstance(UserServiceLocal.class);

        final User expected = new User("pdupont", "pdupont", "1234", "ADMIN", new ArrayList<String>(), "1");
        when(userBusiness.getUser("pdupont")).thenReturn(expected);

        assertNull(testee.getUser(null));
        verify(userBusiness, never()).getUser(anyString());

        assertNull(testee.getUser("pberger"));
        verify(userBusiness).getUser(eq("pberger"));

        assertEquals(expected, testee.getUser("pdupont"));
        verify(userBusiness).getUser(eq("pdupont"));
    }

    @Test
    public void testGetUserAuthorization() {
        setNoRightSubject();
        final UserServiceLocal testee = getInstance(UserServiceLocal.class);

        final String id = "id";
        try {
            testee.getUser(id);
            fail("Expected exception");
        } catch (final ScholagestRuntimeException e) {
            verify(ScholagestThreadLocal.getSubject()).hasRole(anyString());
            verify(ScholagestThreadLocal.getSubject()).isPermitted(id);
        }
    }
}
