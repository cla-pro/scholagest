package net.scholagest.business;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.converter.ClazzEntityConverter;
import net.scholagest.dao.ClazzDaoLocal;
import net.scholagest.dao.PeriodDaoLocal;
import net.scholagest.dao.StudentDaoLocal;
import net.scholagest.dao.TeacherDaoLocal;
import net.scholagest.dao.YearDaoLocal;
import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.db.entity.ClazzEntity;
import net.scholagest.db.entity.PeriodEntity;
import net.scholagest.db.entity.StudentEntity;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.db.entity.YearEntity;
import net.scholagest.object.Clazz;

import com.google.inject.Inject;

/**
 * Implementation of {@link ClazzBusinessLocal}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class ClazzBusinessBean implements ClazzBusinessLocal {
    // public static Map<String, Clazz> classesMap = new HashMap<>();
    //
    // static {
    // classesMap.put("clazz1", new Clazz("clazz1", "1P A", "year1",
    // Arrays.asList("period1", "period2", "period3"),
    // Arrays.asList("teacher1"),
    // Arrays.asList("student1"), Arrays.asList("branch1", "branch2")));
    // classesMap.put("clazz2", new Clazz("clazz2", "2P A", "year2", new
    // ArrayList<String>(), Arrays.asList("teacher2"),
    // Arrays.asList("student2"),
    // new ArrayList<String>()));
    // classesMap.put("clazz3", new Clazz("clazz3", "5P A", "year2", new
    // ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(),
    // new ArrayList<String>()));
    // }

    @Inject
    private ClazzDaoLocal clazzDao;

    @Inject
    private PeriodDaoLocal periodDao;

    @Inject
    private YearDaoLocal yearDao;

    @Inject
    private TeacherDaoLocal teacherDao;

    @Inject
    private StudentDaoLocal studentDao;

    ClazzBusinessBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public Clazz getClazz(final Long id) {
        final ClazzEntity clazzEntity = clazzDao.getClazzEntityById(id);

        if (clazzEntity == null) {
            return null;
        } else {
            final ClazzEntityConverter clazzEntityConverter = new ClazzEntityConverter();
            return clazzEntityConverter.convertToClazz(clazzEntity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clazz createClazz(final Clazz clazz) {
        final ClazzEntityConverter clazzEntityConverter = new ClazzEntityConverter();

        final ClazzEntity clazzEntity = clazzEntityConverter.convertToClazzEntity(clazz);
        final YearEntity yearEntity = yearDao.getYearEntityById(Long.valueOf(clazz.getYear()));
        clazzEntity.setYear(yearEntity);

        final ClazzEntity persistedClazzEntity = clazzDao.persistClazzEntity(clazzEntity);

        final List<PeriodEntity> periodEntityList = createPeriods(persistedClazzEntity);
        final List<PeriodEntity> persistedPeriodEntityList = persistPeriodEntities(periodEntityList);
        persistedClazzEntity.setPeriods(persistedPeriodEntityList);

        return clazzEntityConverter.convertToClazz(persistedClazzEntity);
    }

    private List<PeriodEntity> persistPeriodEntities(final List<PeriodEntity> periodEntityList) {
        final List<PeriodEntity> persistedPeriodEntityList = new ArrayList<>();
        for (final PeriodEntity periodEntity : periodEntityList) {
            final PeriodEntity persistedPeriodEntity = periodDao.persistPeriodEntity(periodEntity);
            persistedPeriodEntityList.add(persistedPeriodEntity);
        }

        return persistedPeriodEntityList;
    }

    private List<PeriodEntity> createPeriods(final ClazzEntity clazzEntity) {
        final List<PeriodEntity> periodEntityList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            final String name = "Trimestre " + (i + 1);
            final PeriodEntity periodEntity = new PeriodEntity();
            periodEntity.setClazz(clazzEntity);
            periodEntity.setName(name);
            periodEntity.setBranchPeriods(new ArrayList<BranchPeriodEntity>());

            periodEntityList.add(periodEntity);
        }

        return periodEntityList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clazz saveClazz(final Clazz clazz) {
        final ClazzEntity clazzEntity = clazzDao.getClazzEntityById(Long.valueOf(clazz.getId()));

        if (clazzEntity == null) {
            return null;
        } else {
            // TODO remove all permissions, update the teachers and students and
            // then insert the permissions
            updateTeachers(clazzEntity, clazz);
            updateStudents(clazzEntity, clazz);

            // TODO create the student results

            clazzEntity.setName(clazz.getName());

            final ClazzEntityConverter clazzEntityConverter = new ClazzEntityConverter();
            return clazzEntityConverter.convertToClazz(clazzEntity);
        }
    }

    private void updateStudents(final ClazzEntity clazzEntity, final Clazz clazz) {
        final List<StudentEntity> studentEntityList = new ArrayList<>();

        for (final String studentId : clazz.getStudents()) {
            final StudentEntity studentEntity = studentDao.getStudentEntityById(Long.valueOf(studentId));
            studentEntityList.add(studentEntity);
        }

        clazzEntity.setStudents(studentEntityList);
    }

    private void updateTeachers(final ClazzEntity clazzEntity, final Clazz clazz) {
        final List<TeacherEntity> teacherEntityList = new ArrayList<>();

        for (final String teacherId : clazz.getTeachers()) {
            final TeacherEntity teacherEntity = teacherDao.getTeacherEntityById(Long.valueOf(teacherId));
            teacherEntityList.add(teacherEntity);
        }

        clazzEntity.setTeachers(teacherEntityList);
    }
    // private void updateStudentResults(final Clazz clazz, final List<String>
    // oldStudentList, final List<String> newStudentList) {
    // if (areListDifferent(oldStudentList, newStudentList)) {
    // for (final String studentId : oldStudentList) {
    // if (!newStudentList.contains(studentId)) {
    // findStudentResultsAndSetInactive(clazz, studentId);
    // }
    // }
    //
    // for (final String studentId : newStudentList) {
    // if (!oldStudentList.contains(studentId)) {
    // createStudentResults(clazz, studentId);
    // }
    // }
    // }
    // }
    //
    // private void findStudentResultsAndSetInactive(final Clazz clazz, final
    // String studentId) {
    // for (final String branchId : clazz.getBranches()) {
    // final Branch branch = BranchBusinessBean.branchesMap.get(branchId);
    //
    // for (final String branchPeriodId : branch.getBranchPeriods()) {
    // final BranchPeriod branchPeriod =
    // BranchPeriodBusinessBean.branchPeriodsMap.get(branchPeriodId);
    //
    // for (final String studentResultId : branchPeriod.getStudentResults()) {
    // final StudentResult studentResult =
    // StudentResultBusinessBean.studentResultsMap.get(studentResultId);
    // if (studentResult.getStudent().equals(studentId)) {
    // studentResult.setActive(false);
    // break;
    // }
    // }
    // }
    // }
    // }
    //
    // private void createStudentResults(final Clazz clazz, final String
    // studentId) {
    // for (final String branchId : clazz.getBranches()) {
    // final Branch branch = BranchBusinessBean.branchesMap.get(branchId);
    //
    // for (final String branchPeriodId : branch.getBranchPeriods()) {
    // final BranchPeriod branchPeriod =
    // BranchPeriodBusinessBean.branchPeriodsMap.get(branchPeriodId);
    //
    // final String studentResultId =
    // IdHelper.getNextId(StudentResultBusinessBean.studentResultsMap.keySet(),
    // "studentResult");
    // final StudentResult studentResult = new StudentResult(studentResultId,
    // studentId, branchPeriodId, new ArrayList<String>(), null, true);
    // StudentResultBusinessBean.studentResultsMap.put(studentResultId,
    // studentResult);
    //
    // final List<String> resultIds = new ArrayList<String>();
    // for (final String examId : branchPeriod.getExams()) {
    // final String resultId =
    // IdHelper.getNextId(ResultBusinessBean.resultsMap.keySet(), "result");
    // final Result result = new Result(resultId, null, examId,
    // studentResultId);
    //
    // ResultBusinessBean.resultsMap.put(resultId, result);
    // resultIds.add(resultId);
    // }
    //
    // final String meanId =
    // IdHelper.getNextId(ResultBusinessBean.resultsMap.keySet(), "result");
    // final Result mean = new Result(meanId, null, branchPeriod.getMeanExam(),
    // studentResultId);
    // ResultBusinessBean.resultsMap.put(meanId, mean);
    //
    // studentResult.setResults(resultIds);
    // studentResult.setMean(meanId);
    //
    // branchPeriod.getStudentResults().add(studentResultId);
    // }
    // }
    // }
    //
    // private void updateTeacherPermissions(final List<String> oldTeacherList,
    // final List<String> newTeacherList, final List<String> oldStudentList,
    // final List<String> newStudentList) {
    // if (areListDifferent(oldStudentList, newStudentList) ||
    // areListDifferent(oldTeacherList, newTeacherList)) {
    // for (final String teacherId : oldTeacherList) {
    // // TODO
    // }
    //
    // for (final String teacherId : newTeacherList) {
    // // TODO
    // }
    // }
    // }
    //
    // private boolean areListDifferent(final List<String> oldStudentList, final
    // List<String> newStudentList) {
    // if (oldStudentList.size() != newStudentList.size()) {
    // return true;
    // } else {
    // for (int i = 0; i < oldStudentList.size(); i++) {
    // if (!oldStudentList.get(i).equals(newStudentList.get(i))) {
    // return true;
    // }
    // }
    // return false;
    // }
    // }
}
