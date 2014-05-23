package net.scholagest.app.rest.ws.authorization;

import net.scholagest.exception.AuthorizationScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
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
                // final WebApplicationException webApplicationException = new
                // WebApplicationException(ResponseUtils.build401UnauthorizedResponse());
                // webApplicationException.printStackTrace();
                // throw webApplicationException;
                throw new AuthorizationScholagestException(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, "The session token is missing");
            }

            final SessionInfo sessionInfo = loginService.authenticateWithSessionId(sessionId);

            ScholagestThreadLocal.setSubject(sessionInfo.getSubject());

            return invocation.proceed();
        } finally {
            ScholagestThreadLocal.setSubject(null);
        }
    }
}
