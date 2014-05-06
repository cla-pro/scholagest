package net.scholagest.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.db.entity.ResultEntity;
import net.scholagest.db.entity.StudentResultEntity;
import net.scholagest.object.StudentResult;

/**
 * Method to convert from the jpa entity {@link StudentResultEntity} to the transfer object {@link StudentResult} and reverse.
 * 
 * @author CLA
 * @since 0.16.0
 */
public class StudentResultEntityConverter {
    /**
     * Convenient method to convert a list of {@link StudentResultEntity} to a list of {@link StudentResult}
     *  
     * @param studentResultEntityList The list to convert
     * @return The converted list
     */
    public List<StudentResult> convertToStudentResultList(final List<StudentResultEntity> studentResultEntityList) {
        final List<StudentResult> studentResultList = new ArrayList<>();

        for (final StudentResultEntity studentResultEntity : studentResultEntityList) {
            studentResultList.add(convertToStudentResult(studentResultEntity));
        }

        return studentResultList;
    }

    /**
     * Convert a {@link StudentResultEntity} to its transfer version {@link StudentResult}.
     * 
     * @param studentResultEntity The student result entity to convert
     * @return The converted student result
     */
    public StudentResult convertToStudentResult(final StudentResultEntity studentResultEntity) {
        final StudentResult studentResult = new StudentResult();
        studentResult.setId("" + studentResultEntity.getId());
        studentResult.setActive(studentResultEntity.isActive());
        studentResult.setStudent("" + studentResultEntity.getStudent().getId());
        studentResult.setBranchPeriod("" + studentResultEntity.getBranchPeriod().getId());
        studentResult.setMean("" + studentResultEntity.getMean().getId());

        final List<String> results = new ArrayList<>();
        for (final ResultEntity resultEntity : studentResultEntity.getResults()) {
            results.add("" + resultEntity.getId());
        }
        studentResult.setResults(results);

        return studentResult;
    }

    /**
     * Convert a {@link StudentResult} to the entity {@link StudentResultEntity}.
     * 
     * @param studentResult The student result to convert
     * @return The converted student result entity
     */
    public StudentResultEntity convertToStudentResultEntity(final StudentResult studentResult) {
        final StudentResultEntity studentResultEntity = new StudentResultEntity();
        studentResultEntity.setActive(studentResult.isActive());
        studentResultEntity.setBranchPeriod(null);
        studentResultEntity.setMean(null);
        studentResultEntity.setStudent(null);
        studentResultEntity.setResults(new ArrayList<ResultEntity>());

        return studentResultEntity;
    }
}
