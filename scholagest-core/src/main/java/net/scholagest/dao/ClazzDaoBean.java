package net.scholagest.dao;

import javax.persistence.EntityManager;

import net.scholagest.db.entity.ClazzEntity;

import com.google.inject.Inject;

/**
 * Implementation of {@link ClazzDaoLocal}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class ClazzDaoBean implements ClazzDaoLocal {
    @Inject
    private EntityManager entityManager;

    ClazzDaoBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public ClazzEntity getClazzEntityById(final long id) {
        return entityManager.find(ClazzEntity.class, Long.valueOf(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClazzEntity persistClazzEntity(final ClazzEntity clazzEntity) {
        entityManager.persist(clazzEntity);
        return clazzEntity;
    }
}
