package net.scholagest.service;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.authorization.RolesAndPermissions;
import net.scholagest.business.BranchPeriodBusinessLocal;
import net.scholagest.object.BranchPeriod;

import com.google.inject.Inject;

/**
 * Implementation of {@link BranchPeriodServiceLocal}. This class's responsibility is to removed the fields
 * the subject does not have access to.
 * 
 * @author CLA
 * @since 0.14.0
 */
public class BranchPeriodServiceBean implements BranchPeriodServiceLocal {
    private final BranchPeriodBusinessLocal branchPeriodBusiness;

    @Inject
    public BranchPeriodServiceBean(final BranchPeriodBusinessLocal branchPeriodBusiness) {
        this.branchPeriodBusiness = branchPeriodBusiness;
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public List<BranchPeriod> getBranchPeriods(final List<String> ids) {
        final List<BranchPeriod> branchPeriodList = new ArrayList<BranchPeriod>();

        for (final String id : ids) {
            final BranchPeriod branchPeriod = branchPeriodBusiness.getBranchPeriod(id);
            if (branchPeriod != null) {
                branchPeriodList.add(branchPeriod);
            }
        }

        return branchPeriodList;
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public BranchPeriod getBranchPeriod(final String id) {
        if (id == null) {
            return null;
        } else {
            return branchPeriodBusiness.getBranchPeriod(id);
        }
    }
}
