package net.scholagest.service;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.authorization.RolesAndPermissions;
import net.scholagest.business.StudentResultBusinessLocal;
import net.scholagest.object.StudentResult;

import com.google.inject.Inject;

/**
 * Implementation of {@link StudentResultServiceLocal}. This class's responsibility is to removed the fields
 * the subject does not have access to.
 * 
 * @author CLA
 * @since 0.14.0
 */
public class StudentResultServiceBean implements StudentResultServiceLocal {

    @Inject
    private StudentResultBusinessLocal studentResultBusiness;

    StudentResultServiceBean() {}

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public List<StudentResult> getStudentResults(final List<String> ids) {
        final List<StudentResult> studentResultList = new ArrayList<>();

        for (final String id : ids) {
            final StudentResult studentResult = studentResultBusiness.getStudentResult(Long.valueOf(id));
            if (studentResult != null) {
                studentResultList.add(studentResult);
            }
        }

        return studentResultList;
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public StudentResult getStudentResult(final String id) {
        if (id == null) {
            return null;
        } else {
            return studentResultBusiness.getStudentResult(Long.valueOf(id));
        }
    }

}
