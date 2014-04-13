package net.scholagest.dao;

import net.scholagest.db.entity.SessionEntity;

/**
 * Provides the methods to handle interaction with the DB for the {@link SessionEntity}. This level is responsible to
 * interacts with the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
public interface SessionDaoLocal {

    /**
     * Get a single session entity identified by its id
     * 
     * @param id The id of the session to retrieve
     * @return The session if it exists, else null
     */
    public SessionEntity getSessionEntityById(final String id);

    /**
     * Persist a new session entity into the DB
     * 
     * @param sessionEntity The session entity to save
     * @return The persisted session entity (with its id)
     */
    public SessionEntity persistSessionEntity(final SessionEntity sessionEntity);
}
