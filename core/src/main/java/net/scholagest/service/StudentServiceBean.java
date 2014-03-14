package net.scholagest.service;

import java.util.List;

import net.scholagest.authorization.Permission;
import net.scholagest.authorization.RolesAndPermissions;
import net.scholagest.business.StudentBusinessLocal;
import net.scholagest.object.Student;

import com.google.inject.Inject;

/**
 * Implementation of {@see StudentServiceLocal}. This class's responsibility is to removed the fields
 * the subject does not have access to.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class StudentServiceBean implements StudentServiceLocal {
    private final StudentBusinessLocal studentBusiness;

    @Inject
    public StudentServiceBean(final StudentBusinessLocal studentBusiness) {
        this.studentBusiness = studentBusiness;
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public List<Student> getStudents() {
        // TODO filter fields
        return studentBusiness.getStudents();
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public Student getStudent(final String id) {
        // TODO filter fields
        if (id == null) {
            return null;
        }

        return studentBusiness.getStudent(id);
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public Student createStudent(final Student student) {
        // TODO filter fields
        if (student == null) {
            return null;
        }

        return studentBusiness.createStudent(student);
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public Student saveStudent(@Permission final String studentId, final Student student) {
        // TODO filter fields
        if (studentId == null || student == null) {
            return null;
        }

        return studentBusiness.saveStudent(studentId, student);
    }

}
