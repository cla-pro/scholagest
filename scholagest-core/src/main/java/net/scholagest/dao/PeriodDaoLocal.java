package net.scholagest.dao;

import net.scholagest.db.entity.PeriodEntity;

/**
 * Provides the methods to handle interaction with the DB for the {@link PeriodEntity}. This level is responsible to
 * interacts with the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
public interface PeriodDaoLocal {
    /**
     * Get a single period entity identified by id
     * 
     * @param id The id of the period to retrieve
     * @return The period if it exists, else null
     */
    public PeriodEntity getPeriodEntityById(long id);

    /**
     * Persist a new period entity into the DB
     * 
     * @param periodEntity The period entity to save
     * @return The persisted period entity (with its id)
     */
    public PeriodEntity persistPeriodEntity(PeriodEntity periodEntity);
}
