package net.scholagest.service;

import net.scholagest.exception.ScholagestException;
import net.scholagest.object.SessionInfo;

/**
 * This service will use the {@see SecurityUtils} to authenticate the token or the username/password. It always return a {@see SessionInfo}
 * that contains:
 * 
 * <ul>
 *   <li>the {@see Subject} used to check the roles and permission during the request processing.</li>
 *   <li>the token used by the web-app to authenticate the next request.</li>
 * </ul>
 * 
 * @author CLA
 * @since
 */
public interface LoginServiceLocal {
    /**
     * Authenticate the username and the password. If they do not match any existing account, an exception is thrown.
     * 
     * @param username Username
     * @param password Password
     * @return The authorization information if the authentication succeed
     * @throws A ScholagestException is thrown if either the username does not exists or the password is false
     */
    public SessionInfo authenticateWithUsername(final String username, final String password) throws ScholagestException;

    /**
     * Authenticate the session id. If the session id does not exists, an exception is thrown.
     * 
     * @param sessionId The session id to authenticate
     * @return The authorization information if the authentication succeed
     * @throws A ScholagestException if the session id does not exists
     */
    public SessionInfo authenticateWithSessionId(final String sessionId) throws ScholagestException;
}
