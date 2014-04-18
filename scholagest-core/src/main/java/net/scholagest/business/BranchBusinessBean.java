package net.scholagest.business;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.converter.BranchEntityConverter;
import net.scholagest.dao.BranchDaoLocal;
import net.scholagest.dao.BranchPeriodDaoLocal;
import net.scholagest.dao.ClazzDaoLocal;
import net.scholagest.dao.StudentResultDaoLocal;
import net.scholagest.db.entity.BranchEntity;
import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.db.entity.ClazzEntity;
import net.scholagest.db.entity.ExamEntity;
import net.scholagest.db.entity.PeriodEntity;
import net.scholagest.db.entity.StudentEntity;
import net.scholagest.db.entity.StudentResultEntity;
import net.scholagest.object.Branch;

import com.google.inject.Inject;

/**
 * Implementation of {@link BranchBusinessLocal}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class BranchBusinessBean implements BranchBusinessLocal {
    // public static Map<String, Branch> branchesMap = new HashMap<>();
    //
    // static {
    // branchesMap.put("branch1", new Branch("branch1", "Math", true, "clazz1",
    // Arrays.asList("branchPeriod1", "branchPeriod3", "branchPeriod5")));
    // branchesMap.put("branch2", new Branch("branch2", "Histoire", false,
    // "clazz1", Arrays.asList("branchPeriod2", "branchPeriod4")));
    // }

    @Inject
    private BranchDaoLocal branchDao;

    @Inject
    private ClazzDaoLocal clazzDao;

    @Inject
    private BranchPeriodDaoLocal branchPeriodDao;

    @Inject
    private StudentResultDaoLocal studentResultDao;

    BranchBusinessBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public Branch getBranch(final Long id) {
        final BranchEntity branchEntity = branchDao.getBranchEntityById(id);

        if (branchEntity == null) {
            return null;
        } else {
            final BranchEntityConverter branchEntityConverter = new BranchEntityConverter();
            return branchEntityConverter.convertToBranch(branchEntity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Branch createBranch(final Branch branch) {
        final BranchEntityConverter branchEntityConverter = new BranchEntityConverter();

        final ClazzEntity clazzEntity = clazzDao.getClazzEntityById(Long.valueOf(branch.getClazz()));

        final BranchEntity branchEntity = branchEntityConverter.convertToBranchEntity(branch);
        branchEntity.setClazz(clazzEntity);

        final BranchEntity persistedBranchEntity = branchDao.persistBranchEntity(branchEntity);

        final List<BranchPeriodEntity> branchPeriodEntityList = createAndPersistBranchPeriod(persistedBranchEntity, clazzEntity);
        branchEntity.setBranchPeriods(branchPeriodEntityList);

        createAndPersistStudentResult(branchPeriodEntityList, clazzEntity);

        return branchEntityConverter.convertToBranch(persistedBranchEntity);
    }

    // private void createStudentResults(final List<BranchPeriod>
    // branchPeriodList, final List<String> students) {
    // for (final BranchPeriod branchPeriod : branchPeriodList) {
    // final List<String> studentResultIdList = new ArrayList<String>();
    //
    // for (final String studentId : students) {
    // final StudentResult studentResult =
    // createSingleStudentResult(branchPeriod, studentId);
    //
    // studentResultIdList.add(studentResult.getId());
    // StudentResultBusinessBean.studentResultsMap.put(studentResult.getId(),
    // studentResult);
    //
    // final String meanId =
    // IdHelper.getNextId(ResultBusinessBean.resultsMap.keySet(), "result");
    // final Result mean = new Result(meanId, null, branchPeriod.getMeanExam(),
    // studentResult.getId());
    // ResultBusinessBean.resultsMap.put(meanId, mean);
    // studentResult.setMean(meanId);
    // }
    //
    // branchPeriod.setStudentResults(studentResultIdList);
    // }
    // }
    //
    // private StudentResult createSingleStudentResult(final BranchPeriod
    // branchPeriod, final String studentId) {
    // final String id =
    // IdHelper.getNextId(StudentResultBusinessBean.studentResultsMap.keySet(),
    // "studentResult");
    // final StudentResult studentResult = new StudentResult();
    // studentResult.setId(id);
    // studentResult.setActive(true);
    // studentResult.setBranchPeriod(branchPeriod.getId());
    // studentResult.setMean(null);
    // studentResult.setResults(new ArrayList<String>());
    // studentResult.setStudent(studentId);
    //
    // return studentResult;
    // }
    //
    // private void updatePeriods(final List<Period> periodList, final
    // List<BranchPeriod> branchPeriodList) {
    // for (final BranchPeriod branchPeriod : branchPeriodList) {
    // final Period period = findPeriod(periodList, branchPeriod.getPeriod());
    // period.getBranchPeriods().add(branchPeriod.getId());
    // }
    // }
    //
    // private Period findPeriod(final List<Period> periodList, final String
    // periodId) {
    // for (final Period period : periodList) {
    // if (period.getId().equals(periodId)) {
    // return period;
    // }
    // }
    // return null;
    // }
    //
    // private List<Period> getPeriodList(final Clazz clazz) {
    // final List<Period> periodList = new ArrayList<Period>();
    //
    // for (final String periodId : clazz.getPeriods()) {
    // final Period period = PeriodBusinessBean.periodsMap.get(periodId);
    // periodList.add(period);
    // }
    //
    // return periodList;
    // }
    //
    // private List<BranchPeriod> createBranchPeriods(final Branch branch, final
    // List<Period> periodList) {
    // final List<BranchPeriod> branchPeriodList = new ArrayList<>();
    //
    // for (final Period period : periodList) {
    // final String id =
    // IdHelper.getNextId(BranchPeriodBusinessBean.branchPeriodsMap.keySet(),
    // "branchPeriod");
    // final BranchPeriod branchPeriod = new BranchPeriod(id, branch.getId(),
    // period.getId(), new ArrayList<String>(), null,
    // new ArrayList<String>());
    //
    // branchPeriodList.add(branchPeriod);
    // BranchPeriodBusinessBean.branchPeriodsMap.put(id, branchPeriod);
    //
    // final String meanExamId =
    // IdHelper.getNextId(ExamBusinessBean.examsMap.keySet(), "exam");
    // final Exam meanExam = new Exam(meanExamId, "Moyenne", 0, id);
    // ExamBusinessBean.examsMap.put(meanExamId, meanExam);
    //
    // branchPeriod.setMeanExam(meanExamId);
    // }
    //
    // return branchPeriodList;
    // }

    private void createAndPersistStudentResult(final List<BranchPeriodEntity> branchPeriodEntityList, final ClazzEntity clazzEntity) {
        for (final BranchPeriodEntity branchPeriodEntity : branchPeriodEntityList) {
            final List<StudentResultEntity> studentResultEntityList = new ArrayList<>();
            for (final StudentEntity studentEntity : clazzEntity.getStudents()) {
                final StudentResultEntity studentResultEntity = new StudentResultEntity();
                studentResultEntity.setActive(true);
                studentResultEntity.setBranchPeriod(branchPeriodEntity);
                studentResultEntity.setStudent(studentEntity);

                studentResultDao.persistStudentResultEntity(studentResultEntity);

                studentResultEntityList.add(studentResultEntity);
            }

            branchPeriodEntity.setStudentResults(studentResultEntityList);
        }
    }

    private List<BranchPeriodEntity> createAndPersistBranchPeriod(final BranchEntity branchEntity, final ClazzEntity clazzEntity) {
        final List<BranchPeriodEntity> branchPeriodEntityList = new ArrayList<>();

        for (final PeriodEntity periodEntity : clazzEntity.getPeriods()) {
            final BranchPeriodEntity branchPeriodEntity = new BranchPeriodEntity();
            branchPeriodEntity.setBranch(branchEntity);
            branchPeriodEntity.setPeriod(periodEntity);
            branchPeriodEntity.setExams(new ArrayList<ExamEntity>());
            branchPeriodEntity.setStudentResults(new ArrayList<StudentResultEntity>());

            branchPeriodDao.persistBranchPeriodEntity(branchPeriodEntity);

            branchPeriodEntityList.add(branchPeriodEntity);

            periodEntity.getBranchPeriods().add(branchPeriodEntity);
            // TODO update period

            // TODO create and persist the student result
            // List<StudentResultEntity> studentResultEntityList =
            // createAndPersistStudentResult(branchPeriodEntity,
            // clazzEntity.getS)
        }

        return branchPeriodEntityList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Branch saveBranch(final Branch branch) {
        final BranchEntity branchEntity = branchDao.getBranchEntityById(Long.valueOf(branch.getId()));

        if (branchEntity == null) {
            return null;
        } else {
            branchEntity.setName(branch.getName());

            final BranchEntityConverter branchEntityConverter = new BranchEntityConverter();
            return branchEntityConverter.convertToBranch(branchEntity);
        }
    }
}
