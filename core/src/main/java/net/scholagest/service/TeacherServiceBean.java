/**
 * 
 */
package net.scholagest.service;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.authorization.Permission;
import net.scholagest.authorization.RolesAndPermissions;
import net.scholagest.business.TeacherBusinessLocal;
import net.scholagest.object.Teacher;
import net.scholagest.object.TeacherDetail;

import com.google.inject.Inject;

/**
 * Implementation of {@link TeacherServiceLocal}. This class's responsibility is to removed the fields
 * the subject does not have access to.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class TeacherServiceBean implements TeacherServiceLocal {

    @Inject
    private TeacherBusinessLocal teacherBusiness;

    TeacherServiceBean() {}

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public List<Teacher> getTeachers() {
        // TODO filter fields
        return teacherBusiness.getTeachers();
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public List<Teacher> getTeacher(final List<String> ids) {
        // TODO filter fields
        final List<Teacher> teachers = new ArrayList<>();

        for (final String id : ids) {
            final Teacher teacher = teacherBusiness.getTeacher(id);
            if (teacher != null) {
                teachers.add(teacher);
            }
        }

        return teachers;
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public Teacher createTeacher(final Teacher teacher) {
        if (teacher == null) {
            return null;
        }

        // TODO create user.
        return teacherBusiness.createTeacher(teacher);
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public Teacher saveTeacher(@Permission final String teacherId, final Teacher teacher) {
        if (teacherId == null || teacher == null) {
            return null;
        }

        // TODO filter fields
        teacher.setId(teacherId);
        return teacherBusiness.saveTeacher(teacher);
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public TeacherDetail getTeacherDetail(final String id) {
        if (id == null) {
            return null;
        }

        // TODO filter fields
        return teacherBusiness.getTeacherDetail(id);
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public TeacherDetail saveTeacherDetail(@Permission final String teacherId, final TeacherDetail teacherDetail) {
        if (teacherId == null || teacherDetail == null) {
            return null;
        }

        // TODO filter fields
        return teacherBusiness.saveTeacherDetail(teacherId, teacherDetail);
    }
}
