package net.scholagest.business;

import net.scholagest.converter.PeriodEntityConverter;
import net.scholagest.dao.PeriodDaoLocal;
import net.scholagest.db.entity.PeriodEntity;
import net.scholagest.object.Period;

import com.google.inject.Inject;

/**
 * Implementation of {@link PeriodBusinessLocal}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class PeriodBusinessBean implements PeriodBusinessLocal {
    // public static Map<String, Period> periodsMap = new HashMap<>();
    //
    // static {
    // periodsMap.put("period1", new Period("period1", "Trimestre 1", "clazz1",
    // Arrays.asList("branchPeriod1", "branchPeriod2")));
    // periodsMap.put("period2", new Period("period2", "Trimestre 2", "clazz1",
    // Arrays.asList("branchPeriod3", "branchPeriod4")));
    // periodsMap.put("period3", new Period("period3", "Trimestre 3", "clazz1",
    // Arrays.asList("branchPeriod5")));
    // }

    @Inject
    private PeriodDaoLocal periodDao;

    PeriodBusinessBean() {}

    @Override
    public Period getPeriod(final Long id) {
        final PeriodEntity periodEntity = periodDao.getPeriodEntityById(id);

        if (periodEntity == null) {
            return null;
        } else {
            final PeriodEntityConverter periodEntityConverter = new PeriodEntityConverter();
            return periodEntityConverter.convertToPeriod(periodEntity);
        }
    }
}
