package net.scholagest.service;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.authorization.Permission;
import net.scholagest.authorization.RolesAndPermissions;
import net.scholagest.business.ExamBusinessLocal;
import net.scholagest.object.Exam;

import com.google.inject.Inject;

/**
 * Implementation of {@link ExamServiceLocal}. This class's responsibility is to removed the fields
 * the subject does not have access to.
 * 
 * @author CLA
 * @since 0.14.0
 */
public class ExamServiceBean implements ExamServiceLocal {

    @Inject
    private ExamBusinessLocal examBusiness;

    ExamServiceBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Exam> getExams(final List<String> ids) {
        final List<Exam> examList = new ArrayList<>();

        for (final String id : ids) {
            final Exam exam = examBusiness.getExam(id);
            if (exam != null) {
                examList.add(exam);
            }
        }

        return examList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Exam getExam(final String id) {
        if (id == null) {
            return null;
        } else {
            return examBusiness.getExam(id);
        }
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public Exam createExam(final Exam exam) {
        if (exam == null) {
            return null;
        } else {
            return examBusiness.createExam(exam);
        }
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public Exam saveExam(@Permission final String id, final Exam exam) {
        if (id == null || exam == null) {
            return null;
        } else {
            return examBusiness.saveExam(exam);
        }
    }

}
