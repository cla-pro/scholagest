package net.scholagest.dao;

import javax.persistence.EntityManager;

import net.scholagest.db.entity.PeriodEntity;

import com.google.inject.Inject;

/**
 * Implementation of {@link PeriodDaoLocal}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class PeriodDaoBean implements PeriodDaoLocal {
    @Inject
    private EntityManager entityManager;

    PeriodDaoBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public PeriodEntity getPeriodEntityById(final long id) {
        return entityManager.find(PeriodEntity.class, Long.valueOf(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PeriodEntity persistPeriodEntity(final PeriodEntity periodEntity) {
        entityManager.persist(periodEntity);
        return periodEntity;
    }
}
