package net.scholagest.dao;

import java.util.List;

import net.scholagest.db.entity.StudentEntity;
import net.scholagest.db.entity.StudentMedicalEntity;
import net.scholagest.db.entity.StudentPersonalEntity;

/**
 * Provides the methods to handle interaction with the DB for the {@link StudentEntity}, {@link StudentPersonalEntity} 
 * and {@link StudentMedicalEntity}. This level is responsible to interacts with the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
public interface StudentDaoLocal {
    /**
     * Get all the student entities from the DB
     * 
     * @return All the student entities
     */
    public List<StudentEntity> getAllStudentEntity();

    /**
     * Get a single student entity identified by id
     * 
     * @param id The id of the student to retrieve
     * @return The student if he exists, else null
     */
    public StudentEntity getStudentEntityById(final long id);

    /**
     * Get the student personal entity with the given  id
     * 
     * @param id Id of the personal information
     * @return The student personal entity
     */
    public StudentPersonalEntity getStudentPersonalEntityById(final long id);

    /**
     * Get the student medical entity with the given  id
     * 
     * @param id Id of the medical information
     * @return The student medical entity
     */
    public StudentMedicalEntity getStudentMedicalEntityById(final long id);

    /**
     * Persist a new student entity into the DB
     * 
     * @param studentEntity The student entity to save
     * @return The persisted student entity (with its id)
     */
    public StudentEntity persistStudentEntity(final StudentEntity studentEntity);
}
