package net.scholagest.business;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.scholagest.object.Period;

/**
 * Implementation of {@link PeriodBusinessLocal}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class PeriodBusinessBean implements PeriodBusinessLocal {
    public static Map<String, Period> periodsMap = new HashMap<>();

    static {
        periodsMap.put("period1", new Period("period1", "Trimestre 1", "clazz1", Arrays.asList("branchPeriod1", "branchPeriod2")));
        periodsMap.put("period2", new Period("period2", "Trimestre 2", "clazz1", Arrays.asList("branchPeriod3", "branchPeriod4")));
        periodsMap.put("period3", new Period("period3", "Trimestre 3", "clazz1", Arrays.asList("branchPeriod5")));
    }

    PeriodBusinessBean() {}

    @Override
    public Period getPeriod(final String id) {
        if (periodsMap.containsKey(id)) {
            return periodsMap.get(id);
        } else {
            return null;
        }
    }
}
