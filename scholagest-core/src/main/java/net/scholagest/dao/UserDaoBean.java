package net.scholagest.dao;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import net.scholagest.db.entity.UserEntity;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Implementation of {@link UserDaoLocal}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class UserDaoBean implements UserDaoLocal {
    @Inject
    private Provider<EntityManager> entityManagerProvider;

    UserDaoBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getUserEntityById(final long id) {
        return entityManagerProvider.get().find(UserEntity.class, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getUserEntityByUsername(final String username) {
        final CriteriaBuilder criteriaBuilder = entityManagerProvider.get().getCriteriaBuilder();
        final CriteriaQuery<UserEntity> criteriaQuery = criteriaBuilder.createQuery(UserEntity.class);

        final Root<UserEntity> userRoot = criteriaQuery.from(UserEntity.class);
        criteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get("username"), username));

        return entityManagerProvider.get().createQuery(criteriaQuery).getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity persistUserEntity(final UserEntity userEntity) {
        entityManagerProvider.get().persist(userEntity);
        return userEntity;
    }
}
