package net.scholagest.dao;

import net.scholagest.db.entity.ClazzEntity;

/**
 * Provides the methods to handle interaction with the DB for the {@link ClazzEntity}. This level is responsible to
 * interacts with the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
public interface ClazzDaoLocal {
    /**
     * Get a single clazz entity identified by id
     * 
     * @param id The id of the clazz to retrieve
     * @return The clazz if it exists, else null
     */
    public ClazzEntity getClazzEntityById(long id);

    /**
     * Persist a new clazz entity into the DB
     * 
     * @param clazzEntity The clazz entity to save
     * @return The persisted clazz entity (with its id)
     */
    public ClazzEntity persistClazzEntity(ClazzEntity clazzEntity);

}
