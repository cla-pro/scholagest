package net.scholagest.dao;

import net.scholagest.db.entity.MeanEntity;

/**
 * Provides the methods to handle interaction with the DB for the {@link MeanEntity}. This level is responsible to
 * interacts with the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
public interface MeanDaoLocal {
    /**
     * Get a single mean entity identified by id
     * 
     * @param id The id of the mean to retrieve
     * @return The mean if it exists, else null
     */
    public MeanEntity getMeanEntityById(long id);

    /**
     * Persist a new mean entity into the DB
     * 
     * @param meanEntity The mean entity to save
     * @return The persisted mean entity (with its id)
     */
    public MeanEntity persistMeanEntity(MeanEntity meanEntity);

}
