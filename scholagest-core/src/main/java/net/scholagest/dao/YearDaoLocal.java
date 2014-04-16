package net.scholagest.dao;

import java.util.List;

import net.scholagest.db.entity.YearEntity;

/**
 * Provides the methods to handle interaction with the DB for the {@link YearEntity}. This level is responsible to
 * interacts with the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
public interface YearDaoLocal {
    /**
     * Get all the year entities from the DB
     * 
     * @return All the year entities
     */
    public List<YearEntity> getAllYearEntity();

    /**
     * Get a single year entity identified by id
     * 
     * @param id The id of the year to retrieve
     * @return The year if it exists, else null
     */
    public YearEntity getYearEntityById(long id);

    /**
     * Persist a new year entity into the DB
     * 
     * @param yearEntity The year entity to save
     * @return The persisted year entity (with its id)
     */
    public YearEntity persistYearEntity(YearEntity yearEntity);

}
