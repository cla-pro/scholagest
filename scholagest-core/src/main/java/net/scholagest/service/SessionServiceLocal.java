package net.scholagest.service;

import net.scholagest.exception.AuthorizationScholagestException;
import net.scholagest.object.SessionInfo;

import org.apache.shiro.SecurityUtils;

/**
 * This service will use the {@link SecurityUtils} to authenticate the token or the username/password. It always return a {@link SessionInfo}
 * that contains:
 * 
 * <ul>
 *   <li>the {@link Subject} used to check the roles and permission during the request processing.</li>
 *   <li>the token used by the web-app to authenticate the next request.</li>
 * </ul>
 * 
 * @author CLA
 * @since
 */
public interface SessionServiceLocal {
    /**
     * Authenticate the username and the password. If they do not match any existing account, an exception is thrown.
     * 
     * @param username Username
     * @param password Password
     * @return The authorization information if the authentication succeed
     * @throws A AuthorizationScholagestException is thrown if either the username does not exists or the password is false
     */
    public SessionInfo authenticateWithUsername(final String username, final String password) throws AuthorizationScholagestException;

    /**
     * Authenticate the session id. If the session id does not exists, an exception is thrown.
     * 
     * @param sessionId The session id to authenticate
     * @return The authorization information if the authentication succeed
     * @throws A AuthorizationScholagestException if the session id does not exists
     */
    public SessionInfo authenticateWithSessionId(final String sessionId) throws AuthorizationScholagestException;
}
