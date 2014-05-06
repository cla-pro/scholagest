package net.scholagest.dao;

import java.util.List;

import net.scholagest.db.entity.TeacherDetailEntity;
import net.scholagest.db.entity.TeacherEntity;

/**
 * Provides the methods to handle interaction with the DB for the {@link TeacherEntity} and {@link TeacherDetailEntity}.
 * This level is responsible to interacts with the DB
 * 
 * @author CLA
 * @since 0.15.0
 */
public interface TeacherDaoLocal {
    /**
     * Get all the teacher entities from the DB
     * 
     * @return All the teacher entities
     */
    public List<TeacherEntity> getAllTeacherEntity();

    /**
     * Get a single teacher entity identified by id
     * 
     * @param id The id of the teacher to retrieve
     * @return The teacher if he exists, else null
     */
    public TeacherEntity getTeacherEntityById(final long id);

    /**
     * Get the teacher detail entity with the given detail id
     * 
     * @param id Id of the detail information
     * @return The teacher detail entity
     */
    public TeacherDetailEntity getTeacherDetailEntityById(final long id);

    /**
     * Persist a new teacher entity into the DB
     * 
     * @param teacherEntity The teacher entity to save
     * @return The persisted teacher entity (with its id)
     */
    public TeacherEntity persistTeacherEntity(final TeacherEntity teacherEntity);
}
