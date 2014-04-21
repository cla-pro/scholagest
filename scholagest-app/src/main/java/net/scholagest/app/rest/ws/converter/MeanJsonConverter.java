package net.scholagest.app.rest.ws.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.app.rest.ws.objects.ResultJson;
import net.scholagest.object.Mean;

/**
 * Method to convert from transfer object {@link Mean} to json {@link ResultJson} and reverse
 * 
 * @author CLA
 * @since 0.16.0
 */
public class MeanJsonConverter {
    /**
     * Convenient method to convert a list of {@link Mean} to a list of {@link ResultJson}
     *  
     * @param meanList The list to convert
     * @return The converted list
     */
    public List<ResultJson> convertToMeanJsonList(final List<Mean> meanList) {
        final List<ResultJson> resultJsonList = new ArrayList<>();

        for (final Mean mean : meanList) {
            resultJsonList.add(convertToMeanJson(mean));
        }

        return resultJsonList;
    }

    /**
     * Convert a {@link Mean} to its json version {@link ResultJson}
     * 
     * @param mean The mean to convert
     * @return The converted mean json
     */
    public ResultJson convertToMeanJson(final Mean mean) {
        final ResultJson meanJson = new ResultJson();

        meanJson.setId(mean.getId());
        meanJson.setGrade(mean.getGrade());
        meanJson.setStudentResult(mean.getStudentResult());

        return meanJson;
    }

    /**
     * Convert a {@link ResultJson} to its version {@link Mean}.
     * 
     * @param meanJson The mean json to convert
     * @return The converted mean
     */
    public Mean convertToMean(final ResultJson meanJson) {
        final Mean mean = new Mean();

        mean.setId(meanJson.getId());
        mean.setGrade(meanJson.getGrade());
        mean.setStudentResult(meanJson.getStudentResult());

        return mean;
    }
}
