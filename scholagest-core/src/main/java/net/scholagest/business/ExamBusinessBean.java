package net.scholagest.business;

import net.scholagest.converter.ExamEntityConverter;
import net.scholagest.dao.BranchPeriodDaoLocal;
import net.scholagest.dao.ExamDaoLocal;
import net.scholagest.dao.ResultDaoLocal;
import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.db.entity.ExamEntity;
import net.scholagest.db.entity.ResultEntity;
import net.scholagest.db.entity.StudentResultEntity;
import net.scholagest.object.Exam;

import com.google.inject.Inject;

/**
 * Implementation of {@link BranchBusinessLocal}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class ExamBusinessBean implements ExamBusinessLocal {
    // public static Map<String, Exam> examsMap = new HashMap<>();
    //
    // static {
    // examsMap.put("exam1", new Exam("exam1", "Récitation 1", 5,
    // "branchPeriod1"));
    // examsMap.put("exam2", new Exam("exam2", "Récitation 2", 4,
    // "branchPeriod1"));
    // examsMap.put("exam6", new Exam("exam6", "Moyenne", 1, "branchPeriod1"));
    // examsMap.put("exam3", new Exam("exam3", "Récitation 3", 3,
    // "branchPeriod4"));
    // examsMap.put("exam4", new Exam("exam4", "Récitation 4", 2,
    // "branchPeriod4"));
    // examsMap.put("exam5", new Exam("exam5", "Récitation 5", 1,
    // "branchPeriod4"));
    // examsMap.put("exam7", new Exam("exam7", "Moyenne", 1, "branchPeriod4"));
    // examsMap.put("exam7", new Exam("exam8", "Moyenne", 1, "branchPeriod2"));
    // examsMap.put("exam7", new Exam("exam9", "Moyenne", 1, "branchPeriod3"));
    // examsMap.put("exam7", new Exam("exam10", "Moyenne", 1, "branchPeriod5"));
    // }

    @Inject
    private ExamDaoLocal examDao;

    @Inject
    private BranchPeriodDaoLocal branchPeriodDao;

    @Inject
    private ResultDaoLocal resultDao;

    ExamBusinessBean() {}

    @Override
    public Exam getExam(final Long id) {
        final ExamEntity examEntity = examDao.getExamEntityById(id);

        if (examEntity == null) {
            return null;
        } else {
            final ExamEntityConverter examEntityConverter = new ExamEntityConverter();
            return examEntityConverter.convertToExam(examEntity);
        }
    }

    @Override
    public Exam createExam(final Exam exam) {
        final ExamEntityConverter examEntityConverter = new ExamEntityConverter();

        final BranchPeriodEntity branchPeriodEntity = branchPeriodDao.getBranchPeriodEntityById(Long.valueOf(exam.getBranchPeriod()));

        final ExamEntity examEntity = examEntityConverter.convertToExamEntity(exam);
        examEntity.setBranchPeriod(branchPeriodEntity);

        final ExamEntity persistedExamEntity = examDao.persistExamEntity(examEntity);

        branchPeriodEntity.getExams().add(persistedExamEntity);
        createResults(examEntity, branchPeriodEntity);

        return examEntityConverter.convertToExam(persistedExamEntity);
    }

    private void createResults(final ExamEntity examEntity, final BranchPeriodEntity branchPeriodEntity) {
        for (final StudentResultEntity studentResultEntity : branchPeriodEntity.getStudentResults()) {
            final ResultEntity resultEntity = new ResultEntity();
            resultEntity.setExam(examEntity);
            resultEntity.setStudentResult(studentResultEntity);

            resultDao.persistResultEntity(resultEntity);

            studentResultEntity.getResults().add(resultEntity);
        }
    }

    @Override
    public Exam saveExam(final Exam exam) {
        final ExamEntity examEntity = examDao.getExamEntityById(Long.valueOf(exam.getId()));

        if (examEntity == null) {
            return null;
        } else {
            examEntity.setName(exam.getName());
            examEntity.setCoeff(exam.getCoeff());

            final ExamEntityConverter examEntityConverter = new ExamEntityConverter();
            return examEntityConverter.convertToExam(examEntity);
        }
    }
}
