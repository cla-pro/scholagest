package net.scholagest.dao;

import javax.persistence.EntityManager;

import net.scholagest.db.entity.BranchPeriodEntity;

import com.google.inject.Inject;

/**
 * Implementation of {@link BranchPeriodDaoLocal}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class BranchPeriodDaoBean implements BranchPeriodDaoLocal {
    @Inject
    private EntityManager entityManager;

    BranchPeriodDaoBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public BranchPeriodEntity getBranchPeriodEntityById(final long id) {
        return entityManager.find(BranchPeriodEntity.class, Long.valueOf(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BranchPeriodEntity persistBranchPeriodEntity(final BranchPeriodEntity branchPeriodEntity) {
        entityManager.persist(branchPeriodEntity);
        return branchPeriodEntity;
    }
}
