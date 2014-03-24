package net.scholagest.service;

import java.util.List;

import net.scholagest.authorization.Permission;
import net.scholagest.authorization.RolesAndPermissions;
import net.scholagest.business.StudentBusinessLocal;
import net.scholagest.object.Student;
import net.scholagest.object.StudentClasses;
import net.scholagest.object.StudentMedical;
import net.scholagest.object.StudentPersonal;

import com.google.inject.Inject;

/**
 * Implementation of {@link StudentServiceLocal}. This class's responsibility is to removed the fields
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

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public StudentPersonal getStudentPersonal(final String id) {
        if (id == null) {
            return null;
        }

        final Student student = studentBusiness.getStudent(id);
        if (student == null) {
            return null;
        } else {
            return student.getStudentPersonal();
        }
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public StudentPersonal saveStudentPersonal(@Permission final String studentId, final StudentPersonal studentPersonal) {
        if (studentId == null || studentPersonal == null) {
            return null;
        }

        return studentBusiness.saveStudentPersonal(studentId, studentPersonal);
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public StudentMedical getStudentMedical(final String id) {
        if (id == null) {
            return null;
        }

        final Student student = studentBusiness.getStudent(id);
        if (student == null) {
            return null;
        } else {
            return student.getStudentMedical();
        }
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public StudentMedical saveStudentMedical(@Permission final String studentId, final StudentMedical studentMedical) {
        if (studentId == null || studentMedical == null) {
            return null;
        }

        return studentBusiness.saveStudentMedical(studentId, studentMedical);
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public StudentClasses getStudentClasses(@Permission final String studentId) {
        if (studentId == null) {
            return null;
        } else {
            return studentBusiness.getStudentClasses(studentId);
        }
    }

}
