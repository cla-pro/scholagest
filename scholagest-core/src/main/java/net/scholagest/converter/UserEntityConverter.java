package net.scholagest.converter;

import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.db.entity.UserEntity;
import net.scholagest.object.User;

/**
 * Method to convert from the jpa entity {@link UserEntity} to the transfer object {@link User}.
 * 
 * @author CLA
 * @since 0.16.0
 */
public class UserEntityConverter {
    /**
     * Convert a {@link UserEntity} to its transfer version {@link User}. The {@link TeacherEntity} is converted as a reference.
     * 
     * @param userEntity The user entity to convert
     * @return The converted user
     */
    public User convertToUser(final UserEntity userEntity) {
        final User user = new User();
        user.setId("" + userEntity.getId());
        user.setUsername(userEntity.getUsername());
        user.setPassword(userEntity.getPassword());
        user.setRole(userEntity.getRole());
        user.setTeacher("" + userEntity.getTeacher().getId());

        return user;
    }
}
