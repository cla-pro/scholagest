package net.scholagest.converter;

import net.scholagest.db.entity.SessionEntity;
import net.scholagest.db.entity.UserEntity;
import net.scholagest.object.Session;

import org.joda.time.DateTime;

/**
 * Method to convert from the jpa entity {@link SessionEntity} to the transfer object {@link Session}.
 * 
 * ! Attention ! The {@link UserEntityConverter} is used inside this class.
 * 
 * @author CLA
 * @since 0.16.0
 */
public class SessionEntityConverter {
    /**
     * Convert a {@link SessionEntity} to its transfer version {@link Session}. The {@link UserEntity} is converted too.
     * 
     * ! Attention ! The {@link UserEntityConverter} is used inside this method.
     * 
     * @param sessionEntity The session entity to convert
     * @return The converted session
     */
    public Session convertToSession(final SessionEntity sessionEntity) {
        final UserEntityConverter userEntityConverter = new UserEntityConverter();

        final Session session = new Session();
        session.setId(sessionEntity.getId());
        session.setExpirationDate(new DateTime(sessionEntity.getExpirationDate()));
        session.setUser(userEntityConverter.convertToUser(sessionEntity.getUser()));

        return session;
    }
}
