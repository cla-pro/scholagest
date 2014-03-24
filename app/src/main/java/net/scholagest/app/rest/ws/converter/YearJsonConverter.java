package net.scholagest.app.rest.ws.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.app.rest.ws.objects.YearJson;
import net.scholagest.object.Year;

/**
 * Method to convert from transfer object {@link Year} to json {@link YearJson} and reverse
 * 
 * @author CLA
 * @since 0.14.0
 */
public class YearJsonConverter {
    /**
     * Convenient method to convert a list of {@link Year} to a list of {@link YearJson}
     *  
     * @param yearList The list to convert
     * @return The converted list
     */
    public List<YearJson> convertToYearJsonList(final List<Year> yearList) {
        final List<YearJson> yearJsonList = new ArrayList<YearJson>();

        for (final Year year : yearList) {
            yearJsonList.add(convertToYearJson(year));
        }

        return yearJsonList;
    }

    /**
     * Convert a {@link Year} to its json version {@link YearJson}
     * 
     * @param year The year to convert
     * @return The converted year json
     */
    public YearJson convertToYearJson(final Year year) {
        final YearJson yearJson = new YearJson();

        yearJson.setId(year.getId());
        yearJson.setName(year.getName());
        yearJson.setRunning(year.isRunning());
        yearJson.setClasses(new ArrayList<>(year.getClasses()));

        return yearJson;
    }

    /**
     * Convert a {@link YearJson} to its version {@link Year}.
     * 
     * @param yearJson The year json to convert
     * @return The converted year
     */
    public Year convertToYear(final YearJson yearJson) {
        final Year year = new Year();

        year.setId(yearJson.getId());
        year.setName(yearJson.getName());
        year.setRunning(yearJson.isRunning());
        year.setClasses(new ArrayList<>(yearJson.getClasses()));

        return year;
    }
}
