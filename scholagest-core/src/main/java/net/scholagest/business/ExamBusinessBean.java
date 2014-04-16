package net.scholagest.business;

import net.scholagest.converter.ExamEntityConverter;
import net.scholagest.dao.ExamDaoLocal;
import net.scholagest.db.entity.ExamEntity;
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

        final ExamEntity examEntity = examEntityConverter.convertToExamEntity(exam);
        final ExamEntity persistedExamEntity = examDao.persistExamEntity(examEntity);

        return examEntityConverter.convertToExam(persistedExamEntity);

        // final String id = IdHelper.getNextId(examsMap.keySet(), "exam");
        // exam.setId(id);
        //
        // examsMap.put(id, exam);
        //
        // final BranchPeriod branchPeriod = updateBranchPeriod(exam);
        // createResults(exam, branchPeriod);
        //
        // return new Exam(exam);
    }

    // private void createResults(final Exam exam, final BranchPeriod
    // branchPeriod) {
    // for (final String studentResultId : branchPeriod.getStudentResults()) {
    // final StudentResult studentResult =
    // StudentResultBusinessBean.studentResultsMap.get(studentResultId);
    //
    // final String id =
    // IdHelper.getNextId(ResultBusinessBean.resultsMap.keySet(), "result");
    // final Result result = new Result();
    // result.setId(id);
    // result.setExam(exam.getId());
    // result.setStudentResult(studentResultId);
    //
    // ResultBusinessBean.resultsMap.put(id, result);
    // studentResult.getResults().add(id);
    // }
    // }
    //
    // private BranchPeriod updateBranchPeriod(final Exam exam) {
    // final BranchPeriod branchPeriod =
    // BranchPeriodBusinessBean.branchPeriodsMap.get(exam.getBranchPeriod());
    // branchPeriod.getExams().add(exam.getId());
    //
    // return branchPeriod;
    // }

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
