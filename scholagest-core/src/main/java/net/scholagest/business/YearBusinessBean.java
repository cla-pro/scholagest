package net.scholagest.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.scholagest.object.Year;
import net.scholagest.utils.IdHelper;

/**
 * Implementation of {@link YearBusinessLocal}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class YearBusinessBean implements YearBusinessLocal {
    public static Map<String, Year> yearsMap = new HashMap<>();

    static {
        yearsMap.put("year1", new Year("year1", "2012-2013", false, Arrays.asList("clazz1")));
        yearsMap.put("year2", new Year("year2", "2013-2014", true, Arrays.asList("clazz2", "clazz3")));
    }

    YearBusinessBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Year> getYears() {
        return copyYears();
    }

    private List<Year> copyYears() {
        final List<Year> years = new ArrayList<>();

        for (final Year year : yearsMap.values()) {
            years.add(new Year(year));
        }

        return years;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Year getYear(final String id) {
        if (yearsMap.containsKey(id)) {
            return new Year(yearsMap.get(id));
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Year createYear(final Year year) {
        final String id = IdHelper.getNextId(yearsMap.keySet(), "year");
        year.setId(id);

        yearsMap.put(id, year);

        return new Year(year);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Year saveYear(final Year year) {
        final Year stored = yearsMap.get(year.getId());

        stored.setName(year.getName());
        stored.setRunning(year.isRunning());
        stored.setClasses(new ArrayList<String>(year.getClasses()));

        return stored;
    }
}
