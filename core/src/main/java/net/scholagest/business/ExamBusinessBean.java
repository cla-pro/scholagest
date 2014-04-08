package net.scholagest.business;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.object.BranchPeriod;
import net.scholagest.object.Exam;
import net.scholagest.object.Result;
import net.scholagest.object.StudentResult;
import net.scholagest.utils.IdHelper;

/**
 * Implementation of {@link BranchBusinessLocal}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class ExamBusinessBean implements ExamBusinessLocal {
    public static Map<String, Exam> examsMap = new HashMap<>();

    static {
        examsMap.put("exam1", new Exam("exam1", "Récitation 1", 5, "branchPeriod1"));
        examsMap.put("exam2", new Exam("exam2", "Récitation 2", 4, "branchPeriod1"));
        examsMap.put("exam6", new Exam("exam6", "Moyenne", 1, "branchPeriod1"));
        examsMap.put("exam3", new Exam("exam3", "Récitation 3", 3, "branchPeriod4"));
        examsMap.put("exam4", new Exam("exam4", "Récitation 4", 2, "branchPeriod4"));
        examsMap.put("exam5", new Exam("exam5", "Récitation 5", 1, "branchPeriod4"));
        examsMap.put("exam7", new Exam("exam7", "Moyenne", 1, "branchPeriod4"));
        examsMap.put("exam7", new Exam("exam8", "Moyenne", 1, "branchPeriod2"));
        examsMap.put("exam7", new Exam("exam9", "Moyenne", 1, "branchPeriod3"));
        examsMap.put("exam7", new Exam("exam10", "Moyenne", 1, "branchPeriod5"));
    }

    ExamBusinessBean() {}

    @Override
    public Exam getExam(final String id) {
        if (examsMap.containsKey(id)) {
            return new Exam(examsMap.get(id));
        } else {
            return null;
        }
    }

    @Override
    public Exam createExam(final Exam exam) {
        final String id = IdHelper.getNextId(examsMap.keySet(), "exam");
        exam.setId(id);

        examsMap.put(id, exam);

        final BranchPeriod branchPeriod = updateBranchPeriod(exam);
        createResults(exam, branchPeriod);

        return new Exam(exam);
    }

    private void createResults(final Exam exam, final BranchPeriod branchPeriod) {
        for (final String studentResultId : branchPeriod.getStudentResults()) {
            final StudentResult studentResult = StudentResultBusinessBean.studentResultsMap.get(studentResultId);

            final String id = IdHelper.getNextId(ResultBusinessBean.resultsMap.keySet(), "result");
            final Result result = new Result();
            result.setId(id);
            result.setExam(exam.getId());
            result.setStudentResult(studentResultId);

            ResultBusinessBean.resultsMap.put(id, result);
            studentResult.getResults().add(id);
        }
    }

    private BranchPeriod updateBranchPeriod(final Exam exam) {
        final BranchPeriod branchPeriod = BranchPeriodBusinessBean.branchPeriodsMap.get(exam.getBranchPeriod());
        branchPeriod.getExams().add(exam.getId());

        return branchPeriod;
    }

    @Override
    public Exam saveExam(final Exam exam) {
        final Exam stored = examsMap.get(exam.getId());

        stored.setName(exam.getName());
        stored.setCoeff(exam.getCoeff());

        return new Exam(stored);
    }
}
