package net.scholagest.app.rest.ws.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.app.rest.ws.objects.ClazzJson;
import net.scholagest.object.Clazz;

/**
 * Method to convert from transfer object {@link Clazz} to json {@link ClazzJson} and reverse
 * 
 * @author CLA
 * @since 0.14.0
 */
public class ClazzJsonConverter {
    /**
     * Convenient method to convert a list of {@link Clazz} to a list of {@link ClazzJson}
     *  
     * @param clazzList The list to convert
     * @return The converted list
     */
    public List<ClazzJson> convertToClazzJsonList(final List<Clazz> clazzList) {
        final List<ClazzJson> clazzJsonList = new ArrayList<>();

        for (final Clazz clazz : clazzList) {
            clazzJsonList.add(convertToClazzJson(clazz));
        }

        return clazzJsonList;
    }

    /**
     * Convert a {@link Clazz} to its json version {@link ClazzJson}
     * 
     * @param clazz The clazz to convert
     * @return The converted clazz json
     */
    public ClazzJson convertToClazzJson(final Clazz clazz) {
        final ClazzJson clazzJson = new ClazzJson();

        clazzJson.setId(clazz.getId());
        clazzJson.setName(clazz.getName());
        clazzJson.setYear(clazz.getYear());
        clazzJson.setPeriods(new ArrayList<>(clazz.getPeriods()));
        clazzJson.setTeachers(new ArrayList<>(clazz.getTeachers()));
        clazzJson.setStudents(new ArrayList<>(clazz.getStudents()));
        clazzJson.setBranches(new ArrayList<>(clazz.getBranches()));

        return clazzJson;
    }

    /**
     * Convert a {@link ClazzJson} to its version {@link Clazz}.
     * 
     * @param clazzJson The clazz json to convert
     * @return The converted clazz
     */
    public Clazz convertToClazz(final ClazzJson clazzJson) {
        final Clazz clazz = new Clazz();

        clazz.setId(clazzJson.getId());
        clazz.setName(clazzJson.getName());
        clazz.setYear(clazzJson.getYear());
        clazz.setPeriods(new ArrayList<>(clazzJson.getPeriods()));
        clazz.setTeachers(new ArrayList<>(clazzJson.getTeachers()));
        clazz.setStudents(new ArrayList<>(clazzJson.getStudents()));
        clazz.setBranches(new ArrayList<>(clazzJson.getBranches()));

        return clazz;
    }
}
