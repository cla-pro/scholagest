package net.scholagest.dao;

import net.scholagest.db.entity.ResultEntity;

/**
 * Provides the methods to handle interaction with the DB for the {@link ResultEntity}. This level is responsible to
 * interacts with the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
public interface ResultDaoLocal {
    /**
     * Get a single result entity identified by id
     * 
     * @param id The id of the result to retrieve
     * @return The result if it exists, else null
     */
    public ResultEntity getResultEntityById(long id);

    /**
     * Persist a new result entity into the DB
     * 
     * @param resultEntity The result entity to save
     * @return The persisted result entity (with its id)
     */
    public ResultEntity persistResultEntity(ResultEntity resultEntity);

}
