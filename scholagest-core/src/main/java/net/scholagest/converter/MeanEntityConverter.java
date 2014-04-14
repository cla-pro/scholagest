package net.scholagest.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.db.entity.MeanEntity;
import net.scholagest.object.Result;

/**
 * Method to convert from the jpa entity {@link MeanEntity} to the transfer object {@link Result} and reverse.
 * 
 * @author CLA
 * @since 0.16.0
 */
public class MeanEntityConverter {
    /**
     * Convenient method to convert a list of {@link MeanEntity} to a list of {@link Result}
     *  
     * @param meanEntityList The list to convert
     * @return The converted list
     */
    public List<Result> convertToMeanList(final List<MeanEntity> meanEntityList) {
        final List<Result> meanList = new ArrayList<>();

        for (final MeanEntity meanEntity : meanEntityList) {
            meanList.add(convertToMean(meanEntity));
        }

        return meanList;
    }

    /**
     * Convert a {@link MeanEntity} to its transfer version {@link Result}.
     * 
     * @param meanEntity The mean entity to convert
     * @return The converted mean
     */
    public Result convertToMean(final MeanEntity meanEntity) {
        final Result mean = new Result();
        mean.setId("" + meanEntity.getId());
        mean.setGrade(meanEntity.getGrade());
        mean.setStudentResult("" + meanEntity.getStudentResult().getId());

        return mean;
    }

    /**
     * Convert a {@link Result} to the entity {@link MeanEntity}.
     * 
     * @param mean The mean to convert
     * @return The converted mean entity
     */
    public MeanEntity convertToMeanEntity(final Result mean) {
        final MeanEntity meanEntity = new MeanEntity();
        meanEntity.setGrade(mean.getGrade());
        meanEntity.setStudentResult(null);

        return meanEntity;
    }
}
