package net.scholagest.dao;

import javax.persistence.EntityManager;

import net.scholagest.db.entity.BranchEntity;

import com.google.inject.Inject;

/**
 * Implementation of {@link BranchDaoLocal}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class BranchDaoBean implements BranchDaoLocal {
    @Inject
    private EntityManager entityManager;

    BranchDaoBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public BranchEntity getBranchEntityById(final long id) {
        return entityManager.find(BranchEntity.class, Long.valueOf(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BranchEntity persistBranchEntity(final BranchEntity branchEntity) {
        entityManager.persist(branchEntity);
        return branchEntity;
    }
}
