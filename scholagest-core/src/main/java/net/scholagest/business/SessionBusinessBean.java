/**
 * 
 */
package net.scholagest.business;

import java.util.UUID;

import net.scholagest.converter.SessionEntityConverter;
import net.scholagest.dao.SessionDaoLocal;
import net.scholagest.dao.UserDaoLocal;
import net.scholagest.db.entity.SessionEntity;
import net.scholagest.db.entity.UserEntity;
import net.scholagest.object.Session;
import net.scholagest.object.User;

import org.joda.time.DateTime;

import com.google.inject.Inject;

/**
 * Implementation of {@link SessionBusinessLocal}.
 * 
 * @author CLA
 * @since 
 */
public class SessionBusinessBean implements SessionBusinessLocal {
    @Inject
    private SessionDaoLocal sessionDao;

    @Inject
    private UserDaoLocal userDao;

    SessionBusinessBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public Session getSession(final String id) {
        final SessionEntity sessionEntity = sessionDao.getSessionEntityById(id);

        if (sessionEntity == null) {
            return null;
        } else {
            final SessionEntityConverter converter = new SessionEntityConverter();
            return converter.convertToSession(sessionEntity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session createSession(final User user) {
        final String id = UUID.randomUUID().toString();
        final DateTime expirationDate = new DateTime().plusHours(2);

        // TODO handle the null-case?
        final UserEntity userEntity = userDao.getUserEntityById(Long.valueOf(user.getId()));

        final SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setId(id);
        sessionEntity.setExpirationDate(expirationDate);
        sessionEntity.setUser(userEntity);

        final SessionEntity persisted = sessionDao.persistSessionEntity(sessionEntity);

        final SessionEntityConverter converter = new SessionEntityConverter();
        return converter.convertToSession(persisted);
    }
}
