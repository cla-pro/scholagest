package net.scholagest.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.scholagest.object.Clazz;
import net.scholagest.object.Period;
import net.scholagest.utils.IdHelper;

/**
 * Implementation of {@link YearBusinessLocal}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class ClazzBusinessBean implements ClazzBusinessLocal {
    public static Map<String, Clazz> classesMap = new HashMap<>();

    static {
        classesMap.put(
                "class1",
                new Clazz("class1", "1P A", "year1", Arrays.asList("1", "2", "3"), Arrays.asList("teacher1"), Arrays.asList("student1"), Arrays
                        .asList("1", "2")));
        classesMap.put("class2", new Clazz("class2", "2P A", "year2", new ArrayList<String>(), Arrays.asList("teacher2"), Arrays.asList("student2"),
                new ArrayList<String>()));
        classesMap.put("class3", new Clazz("class3", "5P A", "year2", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(),
                new ArrayList<String>()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clazz getClazz(final String id) {
        if (classesMap.containsKey(id)) {
            return classesMap.get(id);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clazz createClazz(final Clazz clazz) {
        final String id = IdHelper.getNextId(classesMap.keySet(), "class");
        clazz.setId(id);

        final List<String> periodList = createPeriods(clazz);
        clazz.setPeriods(periodList);

        classesMap.put(id, clazz);

        return new Clazz(clazz);
    }

    private List<String> createPeriods(final Clazz clazz) {
        final List<String> periodIds = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            final String periodId = IdHelper.getNextId(PeriodBusinessBean.periodsMap.keySet(), "period");
            final Period period = new Period(periodId, "Trimestre " + (i + 1), clazz.getId(), new ArrayList<String>());
            PeriodBusinessBean.periodsMap.put(periodId, period);

            periodIds.add(periodId);
        }

        return periodIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clazz saveClazz(final Clazz clazz) {
        final Clazz stored = classesMap.get(clazz.getId());

        // TODO update the permissions

        stored.setName(clazz.getName());
        stored.setStudents(new ArrayList<String>(clazz.getStudents()));
        stored.setTeachers(new ArrayList<String>(clazz.getTeachers()));
        stored.setBranches(new ArrayList<String>(clazz.getBranches()));

        return new Clazz(stored);
    }
}
