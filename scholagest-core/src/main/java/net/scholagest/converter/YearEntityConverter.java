package net.scholagest.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.db.entity.ClazzEntity;
import net.scholagest.db.entity.YearEntity;
import net.scholagest.object.Year;

/**
 * Method to convert from the jpa entity {@link YearEntity} to the transfer object {@link Year} and reverse.
 * 
 * @author CLA
 * @since 0.16.0
 */
public class YearEntityConverter {
    /**
     * Convenient method to convert a list of {@link YearEntity} to a list of {@link Year}
     *  
     * @param yearEntityList The list to convert
     * @return The converted list
     */
    public List<Year> convertToYearList(final List<YearEntity> yearEntityList) {
        final List<Year> yearList = new ArrayList<>();

        for (final YearEntity yearEntity : yearEntityList) {
            yearList.add(convertToYear(yearEntity));
        }

        return yearList;
    }

    /**
     * Convert a {@link YearEntity} to its transfer version {@link Year}.
     * 
     * @param yearEntity The year entity to convert
     * @return The converted year
     */
    public Year convertToYear(final YearEntity yearEntity) {
        final Year year = new Year();
        year.setId("" + yearEntity.getId());
        year.setName(yearEntity.getName());
        year.setRunning(yearEntity.isRunning());

        final List<String> classes = new ArrayList<>();
        for (final ClazzEntity clazzEntity : yearEntity.getClasses()) {
            classes.add("" + clazzEntity.getId());
        }
        year.setClasses(classes);

        return year;
    }

    /**
     * Convert a {@link Year} to the entity {@link YearEntity}.
     * 
     * @param year The year to convert
     * @return The converted year entity
     */
    public YearEntity convertToYearEntity(final Year year) {
        final YearEntity yearEntity = new YearEntity();
        yearEntity.setName(year.getName());
        yearEntity.setRunning(year.isRunning());

        return yearEntity;
    }
}
