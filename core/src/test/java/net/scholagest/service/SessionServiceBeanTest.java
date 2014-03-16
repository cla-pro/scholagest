package net.scholagest.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import net.scholagest.authorization.SessionToken;
import net.scholagest.authorization.UsernameToken;
import net.scholagest.business.SessionBusinessLocal;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.object.SessionInfo;
import net.scholagest.object.User;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.guice.ShiroModule;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Test class for {@see LoginServiceBean}
 * 
 * @author CLA
 * @since 0.13.0
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionServiceBeanTest {
    @Mock
    private SessionBusinessLocal sessionBusiness;

    @Test
    @Ignore
    public void testAuthenticateWithUsernameSuccess() throws ScholagestException {
        final SessionServiceLocal testee = createContextAndGetLoginService(new SuccessRealm());

        final SessionInfo response = testee.authenticateWithUsername("username", "password");
        assertNotNull(response.getToken());
        assertNotNull(response.getSubject());

        verify(sessionBusiness).createSession(any(User.class));
    }

    @Test(expected = ScholagestException.class)
    @Ignore
    public void testAuthenticateWithUsernameFailure() throws ScholagestException {
        final SessionServiceLocal testee = createContextAndGetLoginService(new FailingRealm());

        testee.authenticateWithUsername("username", "password");

        verify(sessionBusiness, never()).createSession(any(User.class));
    }

    @Test
    @Ignore
    public void testAuthenticateWithSessionIdSuccess() throws ScholagestException {
        final SessionServiceLocal testee = createContextAndGetLoginService(new SuccessRealm());

        final SessionInfo response = testee.authenticateWithSessionId(UUID.randomUUID().toString());
        assertNotNull(response.getToken());
        assertNotNull(response.getSubject());

        verify(sessionBusiness, never()).createSession(any(User.class));
    }

    @Test(expected = ScholagestException.class)
    @Ignore
    public void testAuthenticateWithSessionIdFailure() throws ScholagestException {
        final SessionServiceLocal testee = createContextAndGetLoginService(new FailingRealm());

        testee.authenticateWithSessionId(UUID.randomUUID().toString());

        verify(sessionBusiness, never()).createSession(any(User.class));
    }

    public SessionServiceLocal createContextAndGetLoginService(final AuthorizingRealm authorizingRealm) {
        final AbstractModule abstractModule = new AbstractModule() {
            @Override
            protected void configure() {
                bind(SessionServiceLocal.class).to(SessionServiceBean.class);
            }
        };
        final ShiroModule shiroModule = new ShiroModule() {
            @Override
            protected void configureShiro() {
                bindRealm().toInstance(authorizingRealm);
            }
        };

        final Injector injector = Guice.createInjector(abstractModule, shiroModule);
        final org.apache.shiro.mgt.SecurityManager securityManager = injector.getInstance(org.apache.shiro.mgt.SecurityManager.class);
        SecurityUtils.setSecurityManager(securityManager);

        return injector.getInstance(SessionServiceLocal.class);
    }

    private class SuccessRealm extends AuthorizingRealm {
        public SuccessRealm() {
            setAuthenticationCachingEnabled(false);
            setAuthorizationCachingEnabled(false);
            setCredentialsMatcher(new AllowAllCredentialsMatcher());
        }

        @Override
        public boolean supports(final AuthenticationToken token) {
            return token instanceof UsernameToken || token instanceof SessionToken;
        }

        @Override
        protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
            return new SimpleAuthorizationInfo();
        }

        @Override
        protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token) throws AuthenticationException {
            return new SimpleAuthenticationInfo(UUID.randomUUID().toString(), UUID.randomUUID().toString(), getName());
        }
    }

    private class FailingRealm extends AuthorizingRealm {
        public FailingRealm() {
            setAuthenticationCachingEnabled(false);
            setAuthorizationCachingEnabled(false);
            setCredentialsMatcher(new CredentialsMatcher() {
                @Override
                public boolean doCredentialsMatch(final AuthenticationToken token, final AuthenticationInfo info) {
                    return false;
                }
            });
        }

        @Override
        protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
            throw new ScholagestRuntimeException(ScholagestExceptionErrorCode.USER_NOT_FOUND, "");
        }

        @Override
        protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token) throws AuthenticationException {
            throw new ScholagestRuntimeException(ScholagestExceptionErrorCode.SESSION_EXPIRED, "");
        }
    }
}
