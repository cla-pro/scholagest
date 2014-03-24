package net.scholagest.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.scholagest.object.Branch;
import net.scholagest.object.BranchPeriod;
import net.scholagest.object.Clazz;
import net.scholagest.object.Exam;
import net.scholagest.object.Period;
import net.scholagest.object.Result;
import net.scholagest.object.StudentResult;
import net.scholagest.utils.IdHelper;

/**
 * Implementation of {@link BranchBusinessLocal}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class BranchBusinessBean implements BranchBusinessLocal {
    public static Map<String, Branch> branchesMap = new HashMap<>();

    static {
        branchesMap.put("branch1", new Branch("branch1", "Math", true, "clazz1", Arrays.asList("branchPeriod1", "branchPeriod3", "branchPeriod5")));
        branchesMap.put("branch2", new Branch("branch2", "Histoire", false, "clazz1", Arrays.asList("branchPeriod2", "branchPeriod4")));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Branch getBranch(final String id) {
        if (branchesMap.containsKey(id)) {
            return new Branch(branchesMap.get(id));
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Branch createBranch(final Branch branch) {
        final String id = IdHelper.getNextId(branchesMap.keySet(), "branch");

        branch.setId(id);
        branchesMap.put(id, branch);

        final Clazz clazz = ClazzBusinessBean.classesMap.get(branch.getClazz());
        final List<Period> periodList = getPeriodList(clazz);

        final List<BranchPeriod> branchPeriodList = createBranchPeriods(branch, periodList);
        createStudentResults(branchPeriodList, clazz.getStudents());

        updatePeriods(periodList, branchPeriodList);
        clazz.getBranches().add(id);

        return new Branch(branch);
    }

    private void createStudentResults(final List<BranchPeriod> branchPeriodList, final List<String> students) {
        for (final BranchPeriod branchPeriod : branchPeriodList) {
            final List<String> studentResultIdList = new ArrayList<String>();

            for (final String studentId : students) {
                final StudentResult studentResult = createSingleStudentResult(branchPeriod, studentId);

                studentResultIdList.add(studentResult.getId());
                StudentResultBusinessBean.studentResultsMap.put(studentResult.getId(), studentResult);

                final String meanId = IdHelper.getNextId(ResultBusinessBean.resultsMap.keySet(), "result");
                final Result mean = new Result(meanId, null, branchPeriod.getMeanExam(), studentResult.getId());
                ResultBusinessBean.resultsMap.put(meanId, mean);
                studentResult.setMean(meanId);
            }

            branchPeriod.setStudentResults(studentResultIdList);
        }
    }

    private StudentResult createSingleStudentResult(final BranchPeriod branchPeriod, final String studentId) {
        final String id = IdHelper.getNextId(StudentResultBusinessBean.studentResultsMap.keySet(), "studentResult");
        final StudentResult studentResult = new StudentResult();
        studentResult.setId(id);
        studentResult.setActive(true);
        studentResult.setBranchPeriod(branchPeriod.getId());
        studentResult.setMean(null);
        studentResult.setResults(new ArrayList<String>());
        studentResult.setStudent(studentId);

        return studentResult;
    }

    private void updatePeriods(final List<Period> periodList, final List<BranchPeriod> branchPeriodList) {
        for (final BranchPeriod branchPeriod : branchPeriodList) {
            final Period period = findPeriod(periodList, branchPeriod.getPeriod());
            period.getBranchPeriods().add(branchPeriod.getId());
        }
    }

    private Period findPeriod(final List<Period> periodList, final String periodId) {
        for (final Period period : periodList) {
            if (period.getId().equals(periodId)) {
                return period;
            }
        }
        return null;
    }

    private List<Period> getPeriodList(final Clazz clazz) {
        final List<Period> periodList = new ArrayList<Period>();

        for (final String periodId : clazz.getPeriods()) {
            final Period period = PeriodBusinessBean.periodsMap.get(periodId);
            periodList.add(period);
        }

        return periodList;
    }

    private List<BranchPeriod> createBranchPeriods(final Branch branch, final List<Period> periodList) {
        final List<BranchPeriod> branchPeriodList = new ArrayList<>();

        for (final Period period : periodList) {
            final String id = IdHelper.getNextId(BranchPeriodBusinessBean.branchPeriodsMap.keySet(), "branchPeriod");
            final BranchPeriod branchPeriod = new BranchPeriod(id, branch.getId(), period.getId(), new ArrayList<String>(), null,
                    new ArrayList<String>());

            branchPeriodList.add(branchPeriod);
            BranchPeriodBusinessBean.branchPeriodsMap.put(id, branchPeriod);

            final String meanExamId = IdHelper.getNextId(ExamBusinessBean.examsMap.keySet(), "exam");
            final Exam meanExam = new Exam(meanExamId, "Moyenne", 0, id);
            ExamBusinessBean.examsMap.put(meanExamId, meanExam);

            branchPeriod.setMeanExam(meanExamId);
        }

        return branchPeriodList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Branch saveBranch(final Branch branch) {
        final Branch stored = branchesMap.get(branch.getId());

        stored.setName(branch.getName());

        return new Branch(stored);
    }
}
