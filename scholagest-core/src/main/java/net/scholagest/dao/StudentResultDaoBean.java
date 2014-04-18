package net.scholagest.dao;

import javax.persistence.EntityManager;

import net.scholagest.db.entity.StudentResultEntity;

import com.google.inject.Inject;

/**
 * Implementation of {@link StudentResultDaoLocal}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class StudentResultDaoBean implements StudentResultDaoLocal {
    @Inject
    private EntityManager entityManager;

    StudentResultDaoBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentResultEntity getStudentResultEntityById(final long id) {
        return entityManager.find(StudentResultEntity.class, Long.valueOf(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentResultEntity persistStudentResultEntity(final StudentResultEntity yearEntity) {
        entityManager.persist(yearEntity);
        return yearEntity;
    }
}
