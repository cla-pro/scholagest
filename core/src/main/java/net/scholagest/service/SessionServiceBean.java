/**
 * 
 */
package net.scholagest.service;

import net.scholagest.authorization.RealmAuthenticationAndAuthorization;
import net.scholagest.authorization.SessionToken;
import net.scholagest.authorization.UsernameToken;
import net.scholagest.business.SessionBusinessLocal;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.object.Session;
import net.scholagest.object.SessionInfo;
import net.scholagest.object.User;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;

import com.google.inject.Inject;

/**
 * Implementation of {@link SessionServiceLocal}. It uses the Shiro Realm and {@link SecurityUtils} to authenticate the
 * credentials and get the subject which is given back.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class SessionServiceBean implements SessionServiceLocal {

    @Inject
    private SessionBusinessLocal sessionBusiness;

    SessionServiceBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public SessionInfo authenticateWithUsername(final String username, final String password) throws ScholagestException {
        final UsernameToken token = new UsernameToken(username, password);
        final Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(token);

            final User user = getUserFromSubject(subject);
            final Session session = sessionBusiness.createSession(user);

            return new SessionInfo(session.getId(), subject, user);
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

            final User user = getUserFromSubject(subject);
            return new SessionInfo(sessionId, subject, user);
        } catch (final AuthenticationException e) {
            // TODO get the Scholagest exception within
            throw new ScholagestException(ScholagestExceptionErrorCode.USER_NOT_FOUND, "authentication error", e);
        }
    }

    private User getUserFromSubject(final Subject subject) {
        return (User) subject.getPrincipals().fromRealm(RealmAuthenticationAndAuthorization.USER_KEY).iterator().next();
    }
}
