package net.scholagest.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.db.entity.ExamEntity;
import net.scholagest.db.entity.StudentResultEntity;
import net.scholagest.object.BranchPeriod;

/**
 * Method to convert from the jpa entity {@link BranchPeriodEntity} to the transfer object {@link BranchPeriod} and reverse.
 * 
 * @author CLA
 * @since 0.16.0
 */
public class BranchPeriodEntityConverter {
    /**
     * Convenient method to convert a list of {@link BranchPeriodEntity} to a list of {@link BranchPeriod}
     *  
     * @param branchPeriodEntityList The list to convert
     * @return The converted list
     */
    public List<BranchPeriod> convertToBranchPeriodList(final List<BranchPeriodEntity> branchPeriodEntityList) {
        final List<BranchPeriod> branchPeriodList = new ArrayList<>();

        for (final BranchPeriodEntity branchPeriodEntity : branchPeriodEntityList) {
            branchPeriodList.add(convertToBranchPeriod(branchPeriodEntity));
        }

        return branchPeriodList;
    }

    /**
     * Convert a {@link BranchPeriodEntity} to its transfer version {@link BranchPeriod}.
     * 
     * @param branchPeriodEntity The branch period entity to convert
     * @return The converted branch period
     */
    public BranchPeriod convertToBranchPeriod(final BranchPeriodEntity branchPeriodEntity) {
        final BranchPeriod branch = new BranchPeriod();
        branch.setId("" + branchPeriodEntity.getId());
        branch.setBranch("" + branchPeriodEntity.getBranch().getId());
        branch.setPeriod("" + branchPeriodEntity.getPeriod().getId());

        final List<String> studentResults = new ArrayList<>();
        for (final StudentResultEntity studentResultEntity : branchPeriodEntity.getStudentResults()) {
            studentResults.add("" + studentResultEntity.getId());
        }
        branch.setStudentResults(studentResults);

        final List<String> exams = new ArrayList<>();
        for (final ExamEntity examEntity : branchPeriodEntity.getExams()) {
            exams.add("" + examEntity.getId());
        }
        branch.setExams(exams);

        return branch;
    }

    /**
     * Convert a {@link BranchPeriod} to the entity {@link BranchPeriodEntity}.
     * 
     * @param branchPeriod The branch period to convert
     * @return The converted branch period entity
     */
    public BranchPeriodEntity convertToBranchPeriodEntity(final BranchPeriod branchPeriod) {
        final BranchPeriodEntity branchPeriodEntity = new BranchPeriodEntity();
        branchPeriodEntity.setExams(new ArrayList<ExamEntity>());
        branchPeriodEntity.setStudentResults(new ArrayList<StudentResultEntity>());

        return branchPeriodEntity;
    }
}
