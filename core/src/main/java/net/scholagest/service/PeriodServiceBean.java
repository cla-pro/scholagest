package net.scholagest.service;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.authorization.RolesAndPermissions;
import net.scholagest.business.PeriodBusinessLocal;
import net.scholagest.object.Period;

import com.google.inject.Inject;

/**
 * Implementation of {@link PeriodServiceLocal}. This class's responsibility is to removed the fields
 * the subject does not have access to.
 * 
 * @author CLA
 * @since 0.14.0
 */
public class PeriodServiceBean implements PeriodServiceLocal {
    private final PeriodBusinessLocal periodBusiness;

    @Inject
    public PeriodServiceBean(final PeriodBusinessLocal periodBusiness) {
        this.periodBusiness = periodBusiness;
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public List<Period> getPeriods(final List<String> ids) {
        // TODO filter fields
        final List<Period> periods = new ArrayList<>();

        for (final String id : ids) {
            final Period period = periodBusiness.getPeriod(id);
            if (period != null) {
                periods.add(period);
            }
        }

        return periods;
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public Period getPeriod(final String id) {
        if (id == null) {
            return null;
        } else {
            return periodBusiness.getPeriod(id);
        }
    }

}
