package net.scholagest.app.rest.ws.authorization;

import javax.ws.rs.WebApplicationException;

import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.object.SessionInfo;
import net.scholagest.service.SessionServiceLocal;
import net.scholagest.utils.ScholagestThreadLocal;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.inject.Inject;

/**
 * Interceptor used to verify that the authentication token is valid (exists and not expired).
 * 
 * @author CLA
 * @since 0.12.0
 */
public class AuthorizationVerifier implements MethodInterceptor {
    @Inject
    private SessionServiceLocal loginService;

    public AuthorizationVerifier() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        try {
            final String sessionId = ScholagestThreadLocal.getSessionId();
            final SessionInfo sessionInfo = loginService.authenticateWithSessionId(sessionId);

            ScholagestThreadLocal.setSubject(sessionInfo.getSubject());

            return invocation.proceed();
        } catch (final ScholagestException e) {
            throw new WebApplicationException(401);
        } catch (final ScholagestRuntimeException e) {
            throw new WebApplicationException(401);
        } finally {
            ScholagestThreadLocal.setSubject(null);
        }
    }
}
