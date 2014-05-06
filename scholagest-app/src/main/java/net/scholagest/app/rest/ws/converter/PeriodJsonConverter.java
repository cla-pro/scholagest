package net.scholagest.app.rest.ws.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.app.rest.ws.objects.PeriodJson;
import net.scholagest.object.Period;

/**
 * Method to convert from transfer object {@link Period} to json {@link PeriodJson}.
 * 
 * @author CLA
 * @since 0.14.0
 */
public class PeriodJsonConverter {
    /**
     * Convenient method to convert a list of {@link Period} to a list of {@link PeriodJson}
     *  
     * @param periodList The list to convert
     * @return The converted list
     */
    public List<PeriodJson> convertToPeriodJsonList(final List<Period> periodList) {
        final List<PeriodJson> periodJsonList = new ArrayList<>();

        for (final Period period : periodList) {
            periodJsonList.add(convertToPeriodJson(period));
        }

        return periodJsonList;
    }

    /**
     * Convert a {@link Period} to its json version {@link PeriodJson}
     * 
     * @param period The period to convert
     * @return The converted period json
     */
    public PeriodJson convertToPeriodJson(final Period period) {
        final PeriodJson periodJson = new PeriodJson();

        periodJson.setId(period.getId());
        periodJson.setName(period.getName());
        periodJson.setClazz(period.getClazz());
        periodJson.setBranchPeriods(new ArrayList<>(period.getBranchPeriods()));

        return periodJson;
    }
}
