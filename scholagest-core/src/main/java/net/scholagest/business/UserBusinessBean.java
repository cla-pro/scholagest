package net.scholagest.business;

import net.scholagest.converter.UserEntityConverter;
import net.scholagest.dao.UserDaoLocal;
import net.scholagest.db.entity.UserEntity;
import net.scholagest.object.User;

import com.google.inject.Inject;

/**
 * Implementation of {@link UserBusinessLocal}
 * 
 * @author CLA
 * @since 0.13.0
 */
public class UserBusinessBean implements UserBusinessLocal {
    // public static Map<String, User> users = new HashMap<>();
    //
    // static {
    // users.put("clavanchy", new User("clavanchy", "clavanchy", "1234",
    // "ADMIN", Arrays.asList("clavanchy", "teacher1"), "1", "clazz1"));
    // users.put("vparvex", new User("vparvex", "vparvex", "1234", "TEACHER",
    // Arrays.asList("vparvex", "teacher2"), "teacher2", "clazz2"));
    // }

    @Inject
    private UserDaoLocal userDao;

    UserBusinessBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser(final Long id) {
        final UserEntity userEntity = userDao.getUserEntityById(id);

        if (userEntity == null) {
            return null;
        } else {
            final UserEntityConverter converter = new UserEntityConverter();
            return converter.convertToUser(userEntity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserByUsername(final String username) {
        final UserEntity userEntity = userDao.getUserEntityByUsername(username);

        if (userEntity == null) {
            return null;
        } else {
            final UserEntityConverter converter = new UserEntityConverter();
            return converter.convertToUser(userEntity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User saveUser(final User user) {
        final UserEntity userEntity = userDao.getUserEntityById(Long.valueOf(user.getId()));

        if (userEntity == null) {
            return null;
        } else {
            userEntity.setPassword(user.getPassword());
            userEntity.setRole(user.getRole());
            // stored.setPermissions(user.getPermissions());

            final UserEntityConverter converter = new UserEntityConverter();
            return converter.convertToUser(userEntity);
        }
    }
}
