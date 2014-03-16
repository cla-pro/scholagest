package net.scholagest.business;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.scholagest.object.User;

/**
 * Implementation of {@see UserBusinessLocal}
 * 
 * @author CLA
 * @since 0.13.0
 */
public class UserBusinessBean implements UserBusinessLocal {
    private static Map<String, User> users = new HashMap<>();

    static {
        users.put("clavanchy", new User("clavanchy", "clavanchy", "1234", "ADMIN", Arrays.asList("clavanchy", "teacher1"), "teacher1"));
        users.put("vparvex", new User("vparvex", "vparvex", "1234", "TEACHER", Arrays.asList("clavanchy", "teacher2"), "teacher2"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser(final String id) {
        return users.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User saveUser(final String id, final User user) {
        final User stored = getUser(id);

        stored.setPassword(user.getPassword());
        stored.setRole(user.getRole());
        stored.setPermissions(user.getPermissions());

        return new User(stored);
    }
}
