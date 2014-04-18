package net.scholagest.dao;

import javax.persistence.EntityManager;

import net.scholagest.db.entity.ResultEntity;

import com.google.inject.Inject;

/**
 * Implementation of {@link ResultDaoLocal}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class ResultDaoBean implements ResultDaoLocal {
    @Inject
    private EntityManager entityManager;

    ResultDaoBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultEntity getResultEntityById(final long id) {
        return entityManager.find(ResultEntity.class, Long.valueOf(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultEntity persistResultEntity(final ResultEntity resultEntity) {
        entityManager.persist(resultEntity);
        return resultEntity;
    }
}
