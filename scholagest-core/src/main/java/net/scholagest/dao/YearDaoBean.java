package net.scholagest.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

import net.scholagest.db.entity.YearEntity;

import com.google.inject.Inject;

/**
 * Implementation of {@link YearDaoLocal}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class YearDaoBean implements YearDaoLocal {
    @Inject
    private EntityManager entityManager;

    YearDaoBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public List<YearEntity> getAllYearEntity() {
        final CriteriaQuery<YearEntity> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(YearEntity.class);
        criteriaQuery.select(criteriaQuery.from(YearEntity.class));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public YearEntity getYearEntityById(final long id) {
        return entityManager.find(YearEntity.class, Long.valueOf(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public YearEntity persistYearEntity(final YearEntity yearEntity) {
        entityManager.persist(yearEntity);
        return yearEntity;
    }
}
