package net.scholagest.app.rest.ws.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.app.rest.ws.objects.ResultJson;
import net.scholagest.object.Result;

/**
 * Method to convert from transfer object {@link Result} to json {@link ResultJson} and reverse
 * 
 * @author CLA
 * @since 0.14.0
 */
public class ResultJsonConverter {
    /**
     * Convenient method to convert a list of {@link Result} to a list of {@link ResultJson}
     *  
     * @param resultList The list to convert
     * @return The converted list
     */
    public List<ResultJson> convertToResultJsonList(final List<Result> resultList) {
        final List<ResultJson> resultJsonList = new ArrayList<>();

        for (final Result result : resultList) {
            resultJsonList.add(convertToResultJson(result));
        }

        return resultJsonList;
    }

    /**
     * Convert a {@link Result} to its json version {@link ResultJson}
     * 
     * @param result The result to convert
     * @return The converted result json
     */
    public ResultJson convertToResultJson(final Result result) {
        final ResultJson resultJson = new ResultJson();

        resultJson.setId(result.getId());
        resultJson.setGrade(result.getGrade());
        resultJson.setExam(result.getExam());
        resultJson.setStudentResult(result.getStudentResult());

        return resultJson;
    }

    /**
     * Convert a {@link ResultJson} to its version {@link Result}.
     * 
     * @param resultJson The result json to convert
     * @return The converted result
     */
    public Result convertToResult(final ResultJson resultJson) {
        final Result result = new Result();

        result.setId(resultJson.getId());
        result.setGrade(resultJson.getGrade());
        result.setExam(resultJson.getExam());
        result.setStudentResult(resultJson.getStudentResult());

        return result;
    }
}
