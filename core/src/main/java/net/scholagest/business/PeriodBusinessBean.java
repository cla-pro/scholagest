package net.scholagest.business;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.scholagest.object.Period;
import net.scholagest.utils.IdHelper;

/**
 * Implementation of {@link YearBusinessLocal}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class PeriodBusinessBean implements PeriodBusinessLocal {
    public static Map<String, Period> periodsMap = new HashMap<>();

    static {
        periodsMap.put("period1", new Period("period1", "Trimestre 1", "clazz1", Arrays.asList("1", "2")));
        periodsMap.put("period2", new Period("period2", "Trimestre 2", "clazz1", Arrays.asList("3", "4")));
        periodsMap.put("period3", new Period("period3", "Trimestre 3", "clazz1", Arrays.asList("5")));
    }

    @Override
    public Period getPeriod(final String id) {
        if (periodsMap.containsKey(id)) {
            return periodsMap.get(id);
        } else {
            return null;
        }
    }

    @Override
    public Period createPeriod(final Period period) {
        final String id = IdHelper.getNextId(periodsMap.keySet(), "period");

        period.setId(id);
        periodsMap.put(id, period);

        return new Period(period);
    }

    @Override
    public Period savePeriod(final Period period) {
        final Period stored = periodsMap.get(period.getId());

        stored.setName(period.getName());

        return new Period(stored);
    }

}
