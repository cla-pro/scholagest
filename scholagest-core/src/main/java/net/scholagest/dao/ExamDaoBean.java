package net.scholagest.dao;

import javax.persistence.EntityManager;

import net.scholagest.db.entity.ExamEntity;

import com.google.inject.Inject;

/**
 * Implementation of {@link ExamDaoLocal}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class ExamDaoBean implements ExamDaoLocal {
    @Inject
    private EntityManager entityManager;

    ExamDaoBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public ExamEntity getExamEntityById(final long id) {
        return entityManager.find(ExamEntity.class, Long.valueOf(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExamEntity persistExamEntity(final ExamEntity examEntity) {
        entityManager.persist(examEntity);
        return examEntity;
    }
}
