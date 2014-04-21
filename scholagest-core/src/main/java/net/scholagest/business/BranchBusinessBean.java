package net.scholagest.business;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.converter.BranchEntityConverter;
import net.scholagest.dao.BranchDaoLocal;
import net.scholagest.dao.BranchPeriodDaoLocal;
import net.scholagest.dao.ClazzDaoLocal;
import net.scholagest.dao.MeanDaoLocal;
import net.scholagest.dao.StudentResultDaoLocal;
import net.scholagest.db.entity.BranchEntity;
import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.db.entity.ClazzEntity;
import net.scholagest.db.entity.ExamEntity;
import net.scholagest.db.entity.MeanEntity;
import net.scholagest.db.entity.PeriodEntity;
import net.scholagest.db.entity.ResultEntity;
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

    @Inject
    private BranchDaoLocal branchDao;

    @Inject
    private ClazzDaoLocal clazzDao;

    @Inject
    private BranchPeriodDaoLocal branchPeriodDao;

    @Inject
    private StudentResultDaoLocal studentResultDao;

    @Inject
    private MeanDaoLocal meanDao;

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

    private void createAndPersistStudentResult(final List<BranchPeriodEntity> branchPeriodEntityList, final ClazzEntity clazzEntity) {
        for (final BranchPeriodEntity branchPeriodEntity : branchPeriodEntityList) {
            final List<StudentResultEntity> studentResultEntityList = new ArrayList<>();
            for (final StudentEntity studentEntity : clazzEntity.getStudents()) {
                final StudentResultEntity studentResultEntity = new StudentResultEntity();
                studentResultEntity.setActive(true);
                studentResultEntity.setBranchPeriod(branchPeriodEntity);
                studentResultEntity.setStudent(studentEntity);
                studentResultEntity.setResults(new ArrayList<ResultEntity>());

                final StudentResultEntity persistedStudentResultEntity = studentResultDao.persistStudentResultEntity(studentResultEntity);

                final MeanEntity meanEntity = createAndPersistMean(persistedStudentResultEntity);
                persistedStudentResultEntity.setMean(meanEntity);

                studentResultEntityList.add(persistedStudentResultEntity);
            }

            branchPeriodEntity.setStudentResults(studentResultEntityList);
        }
    }

    private MeanEntity createAndPersistMean(final StudentResultEntity persistedStudentResultEntity) {
        final MeanEntity meanEntity = new MeanEntity();
        meanEntity.setStudentResult(persistedStudentResultEntity);

        return meanDao.persistMeanEntity(meanEntity);
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
