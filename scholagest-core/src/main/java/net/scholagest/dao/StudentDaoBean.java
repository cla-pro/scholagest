package net.scholagest.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

import net.scholagest.db.entity.StudentEntity;
import net.scholagest.db.entity.StudentMedicalEntity;
import net.scholagest.db.entity.StudentPersonalEntity;

import com.google.inject.Inject;

/**
 * Implementation of {@link StudentDaoLocal}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class StudentDaoBean implements StudentDaoLocal {
    @Inject
    private EntityManager entityManager;

    StudentDaoBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StudentEntity> getAllStudentEntity() {
        final CriteriaQuery<StudentEntity> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(StudentEntity.class);
        criteriaQuery.select(criteriaQuery.from(StudentEntity.class));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentEntity getStudentEntityById(final long id) {
        return entityManager.find(StudentEntity.class, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentPersonalEntity getStudentPersonalEntityById(final long id) {
        return entityManager.find(StudentPersonalEntity.class, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentMedicalEntity getStudentMedicalEntityById(final long id) {
        return entityManager.find(StudentMedicalEntity.class, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentEntity persistStudentEntity(final StudentEntity studentEntity) {
        entityManager.persist(studentEntity);
        entityManager.persist(studentEntity.getStudentPersonal());
        entityManager.persist(studentEntity.getStudentMedical());

        return studentEntity;
    }
}
