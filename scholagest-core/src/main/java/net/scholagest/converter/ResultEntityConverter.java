package net.scholagest.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.db.entity.ResultEntity;
import net.scholagest.object.Result;

/**
 * Method to convert from the jpa entity {@link ResultEntity} to the transfer object {@link Result} and reverse.
 * 
 * @author CLA
 * @since 0.16.0
 */
public class ResultEntityConverter {
    /**
     * Convenient method to convert a list of {@link ResultEntity} to a list of {@link Result}
     *  
     * @param resultEntityList The list to convert
     * @return The converted list
     */
    public List<Result> convertToResultList(final List<ResultEntity> resultEntityList) {
        final List<Result> examList = new ArrayList<>();

        for (final ResultEntity resultEntity : resultEntityList) {
            examList.add(convertToResult(resultEntity));
        }

        return examList;
    }

    /**
     * Convert a {@link ResultEntity} to its transfer version {@link Result}.
     * 
     * @param resultEntity The result entity to convert
     * @return The converted result
     */
    public Result convertToResult(final ResultEntity resultEntity) {
        final Result exam = new Result();
        exam.setId("" + resultEntity.getId());
        exam.setGrade(resultEntity.getGrade());
        exam.setStudentResult("" + resultEntity.getStudentResult().getId());
        exam.setExam("" + resultEntity.getExam().getId());

        return exam;
    }

    /**
     * Convert a {@link Result} to the entity {@link ResultEntity}.
     * 
     * @param result The result to convert
     * @return The converted result entity
     */
    public ResultEntity convertToResultEntity(final Result result) {
        final ResultEntity resultEntity = new ResultEntity();
        resultEntity.setGrade(result.getGrade());
        resultEntity.setExam(null);
        resultEntity.setStudentResult(null);

        return resultEntity;
    }
}
