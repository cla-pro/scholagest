package net.scholagest.business;

import net.scholagest.converter.StudentResultEntityConverter;
import net.scholagest.dao.StudentResultDaoLocal;
import net.scholagest.db.entity.StudentResultEntity;
import net.scholagest.object.StudentResult;

import com.google.inject.Inject;

/**
 * Implementation of {@link StudentResultBusinessLocal}.
 * 
 * @author CLA
 * @since 0.14.0
 */
public class StudentResultBusinessBean implements StudentResultBusinessLocal {
    // public static Map<String, StudentResult> studentResultsMap = new
    // HashMap<>();
    //
    // static {
    // studentResultsMap.put("studentResult1", new
    // StudentResult("studentResult1", "student1", "branchPeriod1",
    // Arrays.asList("result1", "result2"),
    // "result3", true));
    // studentResultsMap.put("studentResult2",
    // new StudentResult("studentResult2", "student1", "branchPeriod2",
    // Arrays.asList("result4", "result5", "result6"), "result7", true));
    // }

    @Inject
    private StudentResultDaoLocal studentResultDao;

    StudentResultBusinessBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentResult getStudentResult(final Long id) {
        final StudentResultEntity studentResultEntity = studentResultDao.getStudentResultEntityById(id);

        if (studentResultEntity == null) {
            return null;
        } else {
            final StudentResultEntityConverter studentResultEntityConverter = new StudentResultEntityConverter();
            return studentResultEntityConverter.convertToStudentResult(studentResultEntity);
        }
    }
}
