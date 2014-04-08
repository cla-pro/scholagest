package net.scholagest.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.db.entity.TeacherDetailEntity;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.object.Teacher;
import net.scholagest.object.TeacherDetail;

/**
 * Method to convert from the jpa entity {@link TeacherEntity} to the transfer object {@link Teacher} as well as the {@link TeacherDetailEntity} 
 * to {@link TeacherDetail} and reverse.
 * 
 * @author CLA
 * @since 0.15.0
 */
public class TeacherEntityConverter {
    /**
     * Convenient method to convert a list of {@link TeacherEntity} to a list of {@link Teacher}
     *  
     * @param teacherList The list to convert
     * @return The converted list
     */
    public List<Teacher> convertToTeacherList(final List<TeacherEntity> teacherEntityList) {
        final List<Teacher> teacherList = new ArrayList<>();

        for (final TeacherEntity teacherEntity : teacherEntityList) {
            teacherList.add(convertToTeacher(teacherEntity));
        }

        return teacherList;
    }

    /**
     * Convert a {@link TeacherEntity} to its transfer version {@link Teacher}. The {@link TeacherDetailEntity} is converted as well.
     * 
     * @param teacherEntity The teacher entity to convert
     * @return The converted teacher
     */
    public Teacher convertToTeacher(final TeacherEntity teacherEntity) {
        final Teacher teacher = new Teacher();
        teacher.setId("" + teacherEntity.getId());
        teacher.setFirstname(teacherEntity.getFirstname());
        teacher.setLastname(teacherEntity.getLastname());

        final TeacherDetailEntity teacherDetailEntity = teacherEntity.getTeacherDetail();
        final TeacherDetail teacherDetail = teacher.getDetail();
        teacherDetail.setId("" + teacherDetailEntity.getId());
        teacherDetail.setAddress(teacherDetailEntity.getAddress());
        teacherDetail.setEmail(teacherDetailEntity.getEmail());
        teacherDetail.setPhone(teacherDetailEntity.getPhone());

        return teacher;
    }

    /**
     * Convert a {@link Teacher} to the entity {@link TeacherEntity}. The {@link TeacherDetail} is converted as well.
     * 
     * @param teacherEntity The teacher entity to convert
     * @return The converted teacher
     */
    public TeacherEntity convertToTeacherEntity(final Teacher teacher) {
        final TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setFirstname(teacher.getFirstname());
        teacherEntity.setLastname(teacher.getLastname());

        final TeacherDetailEntity teacherDetailEntity = new TeacherDetailEntity();
        final TeacherDetail teacherDetail = teacher.getDetail();
        teacherDetailEntity.setAddress(teacherDetail.getAddress());
        teacherDetailEntity.setEmail(teacherDetail.getEmail());
        teacherDetailEntity.setPhone(teacherDetail.getPhone());

        teacherEntity.setTeacherDetail(teacherDetailEntity);

        return teacherEntity;
    }
}
