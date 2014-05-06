package net.scholagest.business;

import net.scholagest.converter.ResultEntityConverter;
import net.scholagest.dao.ResultDaoLocal;
import net.scholagest.db.entity.ResultEntity;
import net.scholagest.object.Result;

import com.google.inject.Inject;

/**
 * Implementation of {@link ClazzBusinessLocal}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class ResultBusinessBean implements ResultBusinessLocal {
    // public static Map<String, Result> resultsMap = new HashMap<>();
    //
    // static {
    // resultsMap.put("result1", new Result("result1", "3.5", "exam1",
    // "studentResult1"));
    // resultsMap.put("result2", new Result("result2", "5.0", "exam2",
    // "studentResult1"));
    // resultsMap.put("result3", new Result("result3", "4.25", "exam6",
    // "studentResult1"));
    // resultsMap.put("result4", new Result("result4", "3.5", "exam3",
    // "studentResult2"));
    // resultsMap.put("result5", new Result("result5", "5.0", "exam4",
    // "studentResult2"));
    // resultsMap.put("result6", new Result("result6", "5.0", "exam5",
    // "studentResult2"));
    // resultsMap.put("result7", new Result("result7", "4.25", "exam7",
    // "studentResult2"));
    // }

    @Inject
    private ResultDaoLocal resultDao;

    ResultBusinessBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public Result getResult(final Long id) {
        final ResultEntity resultEntity = resultDao.getResultEntityById(id);

        if (resultEntity == null) {
            return null;
        } else {
            final ResultEntityConverter resultEntityConverter = new ResultEntityConverter();
            return resultEntityConverter.convertToResult(resultEntity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result saveResult(final Result result) {
        final ResultEntity resultEntity = resultDao.getResultEntityById(Long.valueOf(result.getId()));

        if (resultEntity == null) {
            return null;
        } else {
            resultEntity.setGrade(result.getGrade());

            final ResultEntityConverter resultEntityConverter = new ResultEntityConverter();
            return resultEntityConverter.convertToResult(resultEntity);
        }
    }
}
