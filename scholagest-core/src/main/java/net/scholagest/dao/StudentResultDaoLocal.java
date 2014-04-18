package net.scholagest.dao;

import net.scholagest.db.entity.StudentResultEntity;

/**
 * Provides the methods to handle interaction with the DB for the {@link StudentResultEntity}. This level is responsible to
 * interacts with the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
public interface StudentResultDaoLocal {
    /**
     * Get a single student result entity identified by id
     * 
     * @param id The id of the student result to retrieve
     * @return The student result if it exists, else null
     */
    public StudentResultEntity getStudentResultEntityById(long id);

    /**
     * Persist a new student result entity into the DB
     * 
     * @param studentResultEntity The student result entity to save
     * @return The persisted student result entity (with its id)
     */
    public StudentResultEntity persistStudentResultEntity(StudentResultEntity studentResultEntity);
}
