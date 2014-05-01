package net.scholagest.tester.utils.creator;

import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.db.entity.UserEntity;

/**
 * Utility class to create {@link UserEntity}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class UserEntityCreator {
    public static UserEntity createUserEntity(final String username, final String password, final String role, final TeacherEntity teacherEntity) {
        final UserEntity userEntity = new UserEntity();
        userEntity.setPassword(password);
        userEntity.setRole(role);
        userEntity.setUsername(username);
        userEntity.setTeacher(teacherEntity);

        return userEntity;
    }
}
