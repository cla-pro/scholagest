package net.scholagest.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.db.entity.PeriodEntity;
import net.scholagest.object.Period;

/**
 * Method to convert from the jpa entity {@link PeriodEntity} to the transfer object {@link Period} and reverse.
 * 
 * @author CLA
 * @since 0.16.0
 */
public class PeriodEntityConverter {
    /**
     * Convenient method to convert a list of {@link PeriodEntity} to a list of {@link Period}
     *  
     * @param periodEntityList The list to convert
     * @return The converted list
     */
    public List<Period> convertToPeriodList(final List<PeriodEntity> periodEntityList) {
        final List<Period> periodList = new ArrayList<>();

        for (final PeriodEntity periodEntity : periodEntityList) {
            periodList.add(convertToPeriod(periodEntity));
        }

        return periodList;
    }

    /**
     * Convert a {@link PeriodEntity} to its transfer version {@link Period}.
     * 
     * @param periodEntity The period entity to convert
     * @return The converted period
     */
    public Period convertToPeriod(final PeriodEntity periodEntity) {
        final Period period = new Period();
        period.setId("" + periodEntity.getId());
        period.setName(periodEntity.getName());
        period.setClazz("" + periodEntity.getClazz().getId());

        final List<String> branchPeriods = new ArrayList<>();
        for (final BranchPeriodEntity branchPeriodEntity : periodEntity.getBranchPeriods()) {
            branchPeriods.add("" + branchPeriodEntity.getId());
        }
        period.setBranchPeriods(branchPeriods);

        return period;
    }

    /**
     * Convert a {@link Period} to the entity {@link PeriodEntity}.
     * 
     * @param period The period to convert
     * @return The converted period entity
     */
    public PeriodEntity convertToPeriodEntity(final Period branch) {
        final PeriodEntity periodEntity = new PeriodEntity();
        periodEntity.setName(branch.getName());
        periodEntity.setClazz(null);
        periodEntity.setBranchPeriods(new ArrayList<BranchPeriodEntity>());

        return periodEntity;
    }
}
