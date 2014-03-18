package net.scholagest.service;

import java.util.List;

import net.scholagest.authorization.RolesAndPermissions;
import net.scholagest.business.YearBusinessLocal;
import net.scholagest.object.Year;

import com.google.inject.Inject;

/**
 * Implementation of {@link StudentServiceLocal}. This class's responsibility is to removed the fields
 * the subject does not have access to.
 * 
 * @author CLA
 * @since
 */
public class YearServiceBean implements YearServiceLocal {
    private final YearBusinessLocal yearBusiness;

    @Inject
    public YearServiceBean(final YearBusinessLocal yearBusiness) {
        this.yearBusiness = yearBusiness;
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public List<Year> getYears() {
        // TODO filter fields
        return yearBusiness.getYears();
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public Year getYear(final String id) {
        // TODO filter fields
        if (id == null) {
            return null;
        } else {
            return yearBusiness.getYear(id);
        }
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public Year createYear(final Year year) {
        if (year == null) {
            return null;
        } else {
            return yearBusiness.createYear(year);
        }
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public Year saveYear(final Year year) {
        if (year == null) {
            return null;
        } else {
            return yearBusiness.saveYear(year);
        }
    }
}
