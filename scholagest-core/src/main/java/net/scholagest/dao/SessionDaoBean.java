package net.scholagest.dao;

import javax.persistence.EntityManager;

import net.scholagest.db.entity.SessionEntity;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Implementation of {@link SessionDaoLocal}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class SessionDaoBean implements SessionDaoLocal {
    @Inject
    private Provider<EntityManager> entityManagerProvider;

    SessionDaoBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public SessionEntity getSessionEntityById(final String id) {
        return entityManagerProvider.get().find(SessionEntity.class, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SessionEntity persistSessionEntity(final SessionEntity sessionEntity) {
        entityManagerProvider.get().persist(sessionEntity);
        return sessionEntity;
    }
}
