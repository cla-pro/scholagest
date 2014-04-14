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
     * @param examEntityList The list to convert
     * @return The converted list
     */
    public List<Result> convertToResultList(final List<ResultEntity> examEntityList) {
        final List<Result> examList = new ArrayList<>();

        for (final ResultEntity examEntity : examEntityList) {
            examList.add(convertToResult(examEntity));
        }

        return examList;
    }

    /**
     * Convert a {@link ResultEntity} to its transfer version {@link Result}.
     * 
     * @param examEntity The exam entity to convert
     * @return The converted exam
     */
    public Result convertToResult(final ResultEntity examEntity) {
        final Result exam = new Result();
        exam.setId("" + examEntity.getId());
        exam.setGrade(examEntity.getGrade());
        exam.setStudentResult("" + examEntity.getStudentResult().getId());
        exam.setExam("" + examEntity.getExam().getId());

        return exam;
    }

    /**
     * Convert a {@link Result} to the entity {@link ResultEntity}.
     * 
     * @param exam The exam to convert
     * @return The converted exam entity
     */
    public ResultEntity convertToResultEntity(final Result exam) {
        final ResultEntity examEntity = new ResultEntity();
        examEntity.setGrade(exam.getGrade());
        examEntity.setExam(null);
        examEntity.setStudentResult(null);

        return examEntity;
    }
}
