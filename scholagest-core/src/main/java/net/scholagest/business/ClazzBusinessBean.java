package net.scholagest.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.scholagest.object.Branch;
import net.scholagest.object.BranchPeriod;
import net.scholagest.object.Clazz;
import net.scholagest.object.Period;
import net.scholagest.object.Result;
import net.scholagest.object.StudentResult;
import net.scholagest.utils.IdHelper;

/**
 * Implementation of {@link ClazzBusinessLocal}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class ClazzBusinessBean implements ClazzBusinessLocal {
    public static Map<String, Clazz> classesMap = new HashMap<>();

    static {
        classesMap.put("clazz1", new Clazz("clazz1", "1P A", "year1", Arrays.asList("period1", "period2", "period3"), Arrays.asList("teacher1"),
                Arrays.asList("student1"), Arrays.asList("branch1", "branch2")));
        classesMap.put("clazz2", new Clazz("clazz2", "2P A", "year2", new ArrayList<String>(), Arrays.asList("teacher2"), Arrays.asList("student2"),
                new ArrayList<String>()));
        classesMap.put("clazz3", new Clazz("clazz3", "5P A", "year2", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(),
                new ArrayList<String>()));
    }

    ClazzBusinessBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public Clazz getClazz(final String id) {
        if (classesMap.containsKey(id)) {
            return new Clazz(classesMap.get(id));
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clazz createClazz(final Clazz clazz) {
        final String id = IdHelper.getNextId(classesMap.keySet(), "class");
        clazz.setId(id);

        final List<String> periodList = createPeriods(clazz);
        clazz.setPeriods(periodList);

        classesMap.put(id, clazz);

        return new Clazz(clazz);
    }

    private List<String> createPeriods(final Clazz clazz) {
        final List<String> periodIds = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            final String periodId = IdHelper.getNextId(PeriodBusinessBean.periodsMap.keySet(), "period");
            final Period period = new Period(periodId, "Trimestre " + (i + 1), clazz.getId(), new ArrayList<String>());
            PeriodBusinessBean.periodsMap.put(periodId, period);

            periodIds.add(periodId);
        }

        return periodIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clazz saveClazz(final Clazz clazz) {
        final Clazz stored = classesMap.get(clazz.getId());

        updateStudentResults(stored, stored.getStudents(), clazz.getStudents());
        updateTeacherPermissions(stored.getTeachers(), clazz.getTeachers(), stored.getStudents(), clazz.getStudents());

        stored.setName(clazz.getName());
        stored.setStudents(new ArrayList<String>(clazz.getStudents()));
        stored.setTeachers(new ArrayList<String>(clazz.getTeachers()));

        return new Clazz(stored);
    }

    private void updateStudentResults(final Clazz clazz, final List<String> oldStudentList, final List<String> newStudentList) {
        if (areListDifferent(oldStudentList, newStudentList)) {
            for (final String studentId : oldStudentList) {
                if (!newStudentList.contains(studentId)) {
                    findStudentResultsAndSetInactive(clazz, studentId);
                }
            }

            for (final String studentId : newStudentList) {
                if (!oldStudentList.contains(studentId)) {
                    createStudentResults(clazz, studentId);
                }
            }
        }
    }

    private void findStudentResultsAndSetInactive(final Clazz clazz, final String studentId) {
        for (final String branchId : clazz.getBranches()) {
            final Branch branch = BranchBusinessBean.branchesMap.get(branchId);

            for (final String branchPeriodId : branch.getBranchPeriods()) {
                final BranchPeriod branchPeriod = BranchPeriodBusinessBean.branchPeriodsMap.get(branchPeriodId);

                for (final String studentResultId : branchPeriod.getStudentResults()) {
                    final StudentResult studentResult = StudentResultBusinessBean.studentResultsMap.get(studentResultId);
                    if (studentResult.getStudent().equals(studentId)) {
                        studentResult.setActive(false);
                        break;
                    }
                }
            }
        }
    }

    private void createStudentResults(final Clazz clazz, final String studentId) {
        for (final String branchId : clazz.getBranches()) {
            final Branch branch = BranchBusinessBean.branchesMap.get(branchId);

            for (final String branchPeriodId : branch.getBranchPeriods()) {
                final BranchPeriod branchPeriod = BranchPeriodBusinessBean.branchPeriodsMap.get(branchPeriodId);

                final String studentResultId = IdHelper.getNextId(StudentResultBusinessBean.studentResultsMap.keySet(), "studentResult");
                final StudentResult studentResult = new StudentResult(studentResultId, studentId, branchPeriodId, new ArrayList<String>(), null, true);
                StudentResultBusinessBean.studentResultsMap.put(studentResultId, studentResult);

                final List<String> resultIds = new ArrayList<String>();
                for (final String examId : branchPeriod.getExams()) {
                    final String resultId = IdHelper.getNextId(ResultBusinessBean.resultsMap.keySet(), "result");
                    final Result result = new Result(resultId, null, examId, studentResultId);

                    ResultBusinessBean.resultsMap.put(resultId, result);
                    resultIds.add(resultId);
                }

                final String meanId = IdHelper.getNextId(ResultBusinessBean.resultsMap.keySet(), "result");
                final Result mean = new Result(meanId, null, branchPeriod.getMeanExam(), studentResultId);
                ResultBusinessBean.resultsMap.put(meanId, mean);

                studentResult.setResults(resultIds);
                studentResult.setMean(meanId);

                branchPeriod.getStudentResults().add(studentResultId);
            }
        }
    }

    private void updateTeacherPermissions(final List<String> oldTeacherList, final List<String> newTeacherList, final List<String> oldStudentList,
            final List<String> newStudentList) {
        if (areListDifferent(oldStudentList, newStudentList) || areListDifferent(oldTeacherList, newTeacherList)) {
            for (final String teacherId : oldTeacherList) {
                // TODO
            }

            for (final String teacherId : newTeacherList) {
                // TODO
            }
        }
    }

    private boolean areListDifferent(final List<String> oldStudentList, final List<String> newStudentList) {
        if (oldStudentList.size() != newStudentList.size()) {
            return true;
        } else {
            for (int i = 0; i < oldStudentList.size(); i++) {
                if (!oldStudentList.get(i).equals(newStudentList.get(i))) {
                    return true;
                }
            }
            return false;
        }
    }
}
