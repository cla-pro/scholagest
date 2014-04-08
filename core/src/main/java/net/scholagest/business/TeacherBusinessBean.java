/**
 * 
 */
package net.scholagest.business;

import java.util.List;

import net.scholagest.converter.TeacherEntityConverter;
import net.scholagest.dao.TeacherDaoLocal;
import net.scholagest.db.entity.TeacherDetailEntity;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.object.Teacher;
import net.scholagest.object.TeacherDetail;

import com.google.inject.Inject;

/**
 * Implementation of {@link TeacherBusinessLocal}.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class TeacherBusinessBean implements TeacherBusinessLocal {
    // public static Map<String, Teacher> teachersMap = new HashMap<>();
    //
    // static {
    // teachersMap.put("teacher1", new Teacher("teacher1", "Cédric", "Lavanchy",
    // new TeacherDetail("teacher1", "Kleefeldstrasse 1, 3018 Bern",
    // "cedric.lavanchy@gmail.com", "+41791234567")));
    // teachersMap.put("teacher2", new Teacher("teacher2", "Valérie", "Parvex",
    // new TeacherDetail("teacher2",
    // "Chemin des Merisiers 25, 1870 Monthey", "valerie.parvex@gmail.com",
    // "+41797654321")));
    // }

    @Inject
    private TeacherDaoLocal teacherDao;

    TeacherBusinessBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Teacher> getTeachers() {
        final TeacherEntityConverter converter = new TeacherEntityConverter();

        final List<TeacherEntity> teacherEntityList = teacherDao.getAllTeacherEntity();

        return converter.convertToTeacherList(teacherEntityList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Teacher getTeacher(final Long id) {
        final TeacherEntity teacherEntity = teacherDao.getTeacherEntityById(id);

        if (teacherEntity == null) {
            return null;
        } else {
            final TeacherEntityConverter converter = new TeacherEntityConverter();
            return converter.convertToTeacher(teacherEntity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Teacher createTeacher(final Teacher teacher) {
        final TeacherEntityConverter converter = new TeacherEntityConverter();

        final TeacherEntity teacherEntity = converter.convertToTeacherEntity(teacher);
        final TeacherEntity persistedTeacherEntity = teacherDao.persistTeacherEntity(teacherEntity);

        return converter.convertToTeacher(persistedTeacherEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Teacher saveTeacher(final Teacher teacher) {
        final TeacherEntity teacherEntity = teacherDao.getTeacherEntityById(Long.valueOf(teacher.getId()));

        if (teacherEntity == null) {
            return null;
        } else {
            final TeacherEntityConverter converter = new TeacherEntityConverter();
            teacherEntity.setFirstname(teacher.getFirstname());
            teacherEntity.setLastname(teacher.getLastname());

            return converter.convertToTeacher(teacherEntity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeacherDetail getTeacherDetail(final Long id) {
        final TeacherDetailEntity teacherDetailEntity = teacherDao.getTeacherDetailEntityById(id);

        if (teacherDetailEntity == null) {
            return null;
        } else {
            final TeacherEntityConverter converter = new TeacherEntityConverter();
            final Teacher teacher = converter.convertToTeacher(teacherDetailEntity.getTeacher());
            return teacher.getDetail();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeacherDetail saveTeacherDetail(final TeacherDetail teacherDetail) {
        final TeacherDetailEntity teacherDetailEntity = teacherDao.getTeacherDetailEntityById(Long.valueOf(teacherDetail.getId()));

        if (teacherDetailEntity == null) {
            return null;
        } else {
            teacherDetailEntity.setAddress(teacherDetail.getAddress());
            teacherDetailEntity.setEmail(teacherDetail.getEmail());
            teacherDetailEntity.setPhone(teacherDetail.getPhone());

            final TeacherEntityConverter converter = new TeacherEntityConverter();
            final Teacher teacher = converter.convertToTeacher(teacherDetailEntity.getTeacher());

            return teacher.getDetail();
        }
    }
}
