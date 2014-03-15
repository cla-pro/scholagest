/**
 * 
 */
package net.scholagest.service;

import java.util.UUID;

import net.scholagest.authorization.SessionToken;
import net.scholagest.authorization.UsernameToken;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.object.SessionInfo;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;

/**
 * Implementation of {@see LoginServiceLocal}. It uses the Shiro Realm and {@see SecurityUtils} to authenticate the
 * credentials and get the subject which is given back.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class LoginServiceBean implements LoginServiceLocal {
    /**
     * {@inheritDoc}
     */
    @Override
    public SessionInfo authenticateWithUsername(final String username, final String password) throws ScholagestException {
        final String sessionToken = UUID.randomUUID().toString();

        final UsernameToken token = new UsernameToken(sessionToken, username, password);
        final Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(token);

            // TODO Store the session into the db
            return new SessionInfo(sessionToken, subject);
        } catch (final AuthenticationException e) {
            // TODO get the Scholagest exception within
            throw new ScholagestException(ScholagestExceptionErrorCode.USER_NOT_FOUND, "authentication error", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SessionInfo authenticateWithSessionId(final String sessionId) throws ScholagestException {
        final SessionToken token = new SessionToken(sessionId);
        final Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(token);
            // TODO Optionally update the session into the db
            return new SessionInfo(sessionId, subject);
        } catch (final AuthenticationException e) {
            // TODO get the Scholagest exception within
            throw new ScholagestException(ScholagestExceptionErrorCode.USER_NOT_FOUND, "authentication error", e);
        }
    }
}
