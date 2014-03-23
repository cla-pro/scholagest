package net.scholagest.business;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.object.Result;

/**
 * Implementation of {@link ClazzBusinessLocal}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class ResultBusinessBean implements ResultBusinessLocal {
    public static Map<String, Result> resultsMap = new HashMap<>();

    static {
        resultsMap.put("result1", new Result("result1", 3.5, "exam1", "studentResult1"));
        resultsMap.put("result2", new Result("result2", 5.0, "exam2", "studentResult1"));
        resultsMap.put("result3", new Result("result3", 4.25, "exam6", "studentResult1"));
        resultsMap.put("result4", new Result("result4", 3.5, "exam3", "studentResult2"));
        resultsMap.put("result5", new Result("result5", 5.0, "exam4", "studentResult2"));
        resultsMap.put("result6", new Result("result6", 5.0, "exam5", "studentResult2"));
        resultsMap.put("result7", new Result("result7", 4.25, "exam7", "studentResult2"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result getResult(final String id) {
        if (resultsMap.containsKey(id)) {
            return resultsMap.get(id);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result saveResult(final Result result) {
        final Result stored = resultsMap.get(result.getId());
        stored.setGrade(result.getGrade());

        return new Result(stored);
    }

}
