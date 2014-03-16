/**
 * 
 */
package net.scholagest.business;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.scholagest.object.Session;
import net.scholagest.object.User;

import org.joda.time.DateTime;

/**
 * Implementation of {@see SessionBusinessLocal}.
 * 
 * @author CLA
 * @since 
 */
public class SessionBusinessBean implements SessionBusinessLocal {
    private static final Map<String, Session> sessionMap = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public Session getSession(final String id) {
        return sessionMap.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session createSession(final User user) {
        final String id = UUID.randomUUID().toString();
        final DateTime expirationDate = new DateTime().plusHours(2);

        final Session session = new Session(id, expirationDate, user);
        sessionMap.put(id, session);

        return session;
    }
}
