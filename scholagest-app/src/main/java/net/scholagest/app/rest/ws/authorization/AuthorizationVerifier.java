package net.scholagest.app.rest.ws.authorization;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.object.SessionInfo;
import net.scholagest.service.SessionServiceLocal;
import net.scholagest.utils.ScholagestThreadLocal;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

/**
 * Interceptor used to verify that the authentication token is valid (exists and not expired).
 * 
 * @author CLA
 * @since 0.12.0
 */
public class AuthorizationVerifier implements MethodInterceptor {
    @Inject
    private SessionServiceLocal loginService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        try {
            final String sessionId = ScholagestThreadLocal.getSessionId();

            if (sessionId == null) {
                final WebApplicationException webApplicationException = new WebApplicationException(buildUnauthorizedResponse());
                webApplicationException.printStackTrace();
                throw webApplicationException;
            }

            final SessionInfo sessionInfo = loginService.authenticateWithSessionId(sessionId);

            ScholagestThreadLocal.setSubject(sessionInfo.getSubject());

            return invocation.proceed();
        } catch (final ScholagestException e) {
            e.printStackTrace();
            throw new WebApplicationException(buildUnauthorizedResponse());
        } catch (final ScholagestRuntimeException e) {
            e.printStackTrace();
            throw new WebApplicationException(buildUnauthorizedResponse());
        } finally {
            ScholagestThreadLocal.setSubject(null);
        }
    }

    private Response buildUnauthorizedResponse() {
        return Response.status(Status.UNAUTHORIZED).header("WWW-Authenticate", "Basic realm=\"test\"").build();
    }
}
