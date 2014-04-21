package net.scholagest.dao;

import javax.persistence.EntityManager;

import net.scholagest.db.entity.MeanEntity;

import com.google.inject.Inject;

/**
 * Implementation of {@link MeanDaoLocal}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class MeanDaoBean implements MeanDaoLocal {
    @Inject
    private EntityManager entityManager;

    MeanDaoBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public MeanEntity getMeanEntityById(final long id) {
        return entityManager.find(MeanEntity.class, Long.valueOf(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MeanEntity persistMeanEntity(final MeanEntity meanEntity) {
        entityManager.persist(meanEntity);
        return meanEntity;
    }
}
