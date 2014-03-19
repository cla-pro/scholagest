package net.scholagest.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.scholagest.object.Clazz;
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
        classesMap.put(id, clazz);

        // TODO create the periods

        return new Clazz(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clazz saveClazz(final Clazz clazz) {
        final Clazz stored = classesMap.get(clazz.getId());

        // TODO update the permissions

        stored.setName(clazz.getName());
        stored.setStudents(clazz.getStudents());
        stored.setTeachers(clazz.getTeachers());

        return new Clazz(stored);
    }
}
