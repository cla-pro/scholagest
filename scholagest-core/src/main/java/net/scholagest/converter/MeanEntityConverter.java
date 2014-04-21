package net.scholagest.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.db.entity.MeanEntity;
import net.scholagest.object.Mean;

/**
 * Method to convert from the jpa entity {@link MeanEntity} to the transfer object {@link Mean} and reverse.
 * 
 * @author CLA
 * @since 0.16.0
 */
public class MeanEntityConverter {
    /**
     * Convenient method to convert a list of {@link MeanEntity} to a list of {@link Mean}
     *  
     * @param meanEntityList The list to convert
     * @return The converted list
     */
    public List<Mean> convertToMeanList(final List<MeanEntity> meanEntityList) {
        final List<Mean> meanList = new ArrayList<>();

        for (final MeanEntity meanEntity : meanEntityList) {
            meanList.add(convertToMean(meanEntity));
        }

        return meanList;
    }

    /**
     * Convert a {@link MeanEntity} to its transfer version {@link Mean}.
     * 
     * @param meanEntity The mean entity to convert
     * @return The converted mean
     */
    public Mean convertToMean(final MeanEntity meanEntity) {
        final Mean mean = new Mean();
        mean.setId("" + meanEntity.getId());
        mean.setGrade(meanEntity.getGrade());
        mean.setStudentResult("" + meanEntity.getStudentResult().getId());

        return mean;
    }

    /**
     * Convert a {@link Mean} to the entity {@link MeanEntity}.
     * 
     * @param mean The mean to convert
     * @return The converted mean entity
     */
    public MeanEntity convertToMeanEntity(final Mean mean) {
        final MeanEntity meanEntity = new MeanEntity();
        meanEntity.setGrade(mean.getGrade());
        meanEntity.setStudentResult(null);

        return meanEntity;
    }
}
