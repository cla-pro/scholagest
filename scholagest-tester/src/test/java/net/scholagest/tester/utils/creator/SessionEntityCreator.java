package net.scholagest.tester.utils.creator;

import net.scholagest.db.entity.SessionEntity;
import net.scholagest.db.entity.UserEntity;

import org.joda.time.DateTime;

/**
 * Utility class to create {@link SessionEntity}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class SessionEntityCreator {
    public static SessionEntity createSessionEntity(final String id, final DateTime expirationDate, final UserEntity userEntity) {
        final SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setId(id);
        sessionEntity.setExpirationDate(expirationDate);
        sessionEntity.setUser(userEntity);

        return sessionEntity;
    }
}
