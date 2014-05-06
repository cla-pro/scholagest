package net.scholagest.dao;

import net.scholagest.db.entity.ExamEntity;

/**
 * Provides the methods to handle interaction with the DB for the {@link ExamEntity}. This level is responsible to
 * interacts with the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
public interface ExamDaoLocal {
    /**
     * Get a single exam entity identified by id
     * 
     * @param id The id of the exam to retrieve
     * @return The exam if it exists, else null
     */
    public ExamEntity getExamEntityById(long id);

    /**
     * Persist a new exam entity into the DB
     * 
     * @param examEntity The exam entity to save
     * @return The persisted exam entity (with its id)
     */
    public ExamEntity persistExamEntity(ExamEntity examEntity);

}
