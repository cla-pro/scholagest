package net.scholagest.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

import net.scholagest.db.entity.TeacherDetailEntity;
import net.scholagest.db.entity.TeacherEntity;

import com.google.inject.Inject;

/**
 * Implementation of {@link TeacherDaoLocal}
 * 
 * @author CLA
 * @since 0.15.0
 */
public class TeacherDaoBean implements TeacherDaoLocal {
    @Inject
    private EntityManager entityManager;

    TeacherDaoBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TeacherEntity> getAllTeacherEntity() {
        final CriteriaQuery<TeacherEntity> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(TeacherEntity.class);
        criteriaQuery.select(criteriaQuery.from(TeacherEntity.class));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeacherEntity getTeacherEntityById(final long id) {
        return entityManager.find(TeacherEntity.class, Long.valueOf(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeacherDetailEntity getTeacherDetailEntityById(final long id) {
        return entityManager.find(TeacherDetailEntity.class, Long.valueOf(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeacherEntity persistTeacherEntity(final TeacherEntity teacherEntity) {
        // Must be stored first otherwise, the reference is wrong
        entityManager.persist(teacherEntity);
        entityManager.persist(teacherEntity.getTeacherDetail());

        return teacherEntity;
    }
}
