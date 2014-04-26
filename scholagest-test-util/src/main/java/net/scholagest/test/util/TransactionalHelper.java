package net.scholagest.test.util;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class TransactionalHelper {
    @Inject
    private EntityManager entityManager;

    @Transactional
    public void executeInTransaction(final Runnable runnable) {
        runnable.run();
    }

    @Transactional
    public <T> T persistEntity(final T entity) {
        if (entityManager.contains(entity)) {
            return entityManager.merge(entity);
        } else {
            entityManager.persist(entity);
            return entity;
        }
    }
}
