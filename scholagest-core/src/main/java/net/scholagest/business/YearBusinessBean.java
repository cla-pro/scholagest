package net.scholagest.business;

import java.util.List;

import net.scholagest.converter.YearEntityConverter;
import net.scholagest.dao.YearDaoLocal;
import net.scholagest.db.entity.YearEntity;
import net.scholagest.object.Year;

import com.google.inject.Inject;

/**
 * Implementation of {@link YearBusinessLocal}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class YearBusinessBean implements YearBusinessLocal {
    // public static Map<String, Year> yearsMap = new HashMap<>();
    //
    // static {
    // yearsMap.put("year1", new Year("year1", "2012-2013", false,
    // Arrays.asList("clazz1")));
    // yearsMap.put("year2", new Year("year2", "2013-2014", true,
    // Arrays.asList("clazz2", "clazz3")));
    // }

    @Inject
    private YearDaoLocal yearDao;

    YearBusinessBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Year> getYears() {
        final YearEntityConverter yearEntityConverter = new YearEntityConverter();

        final List<YearEntity> yearEntityList = yearDao.getAllYearEntity();

        return yearEntityConverter.convertToYearList(yearEntityList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Year getYear(final Long id) {
        final YearEntity yearEntity = yearDao.getYearEntityById(id);

        if (yearEntity == null) {
            return null;
        } else {
            final YearEntityConverter yearEntityConverter = new YearEntityConverter();
            return yearEntityConverter.convertToYear(yearEntity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Year createYear(final Year year) {
        final YearEntityConverter yearEntityConverter = new YearEntityConverter();

        final YearEntity yearEntity = yearEntityConverter.convertToYearEntity(year);
        final YearEntity persistedYearEntity = yearDao.persistYearEntity(yearEntity);

        return yearEntityConverter.convertToYear(persistedYearEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Year saveYear(final Year year) {
        final YearEntityConverter yearEntityConverter = new YearEntityConverter();
        final YearEntity yearEntity = yearDao.getYearEntityById(Long.valueOf(year.getId()));

        if (yearEntity == null) {
            return null;
        } else {
            yearEntity.setName(year.getName());
            yearEntity.setRunning(year.isRunning());
            // The class list is updated when the class is created.

            return yearEntityConverter.convertToYear(yearEntity);
        }
    }
}
