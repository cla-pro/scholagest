package net.scholagest.business;

import net.scholagest.object.Session;
import net.scholagest.object.User;

/**
 * Provides the methods to handle the sessions. This level is responsible to
 * read the elements from the DB and convert them to the transfer objects.
 * 
 * @author CLA
 * @since 0.13.0
 */
public interface SessionBusinessLocal {
    /**
     * Retrieve the session by id.
     * 
     * @param id The session's id
     * @return The session
     */
    public Session getSession(final String id);

    /**
     * Create a session for the given user. The id and the expiration date are set
     * in this method.
     * 
     * @param user The user for which the session is created
     * @return The session
     */
    public Session createSession(final User user);
}
