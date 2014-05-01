package net.scholagest.tester.utils.creator;

import java.util.ArrayList;

import net.scholagest.db.entity.ClazzEntity;
import net.scholagest.db.entity.TeacherDetailEntity;
import net.scholagest.db.entity.TeacherEntity;

/**
 * Utility class to create {@link TeacherEntity} and {@link TeacherDetailEntity}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class TeacherEntityCreator {
    public static TeacherEntity createTeacherEntity(final String firstname, final String lastname, final TeacherDetailEntity teacherDetailEntity) {
        final TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setFirstname(firstname);
        teacherEntity.setLastname(lastname);
        if (teacherDetailEntity != null) {
            teacherEntity.setTeacherDetail(teacherDetailEntity);
        }
        teacherEntity.setClazzes(new ArrayList<ClazzEntity>());

        return teacherEntity;
    }

    public static TeacherDetailEntity createTeacherDetailEntity(final String address, final String email, final String phone) {
        final TeacherDetailEntity teacherDetailEntity = new TeacherDetailEntity();
        teacherDetailEntity.setAddress(address);
        teacherDetailEntity.setEmail(email);
        teacherDetailEntity.setPhone(phone);

        return teacherDetailEntity;
    }
}
