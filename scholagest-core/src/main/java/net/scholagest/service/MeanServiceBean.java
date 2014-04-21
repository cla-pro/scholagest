package net.scholagest.service;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.authorization.RolesAndPermissions;
import net.scholagest.business.MeanBusinessLocal;
import net.scholagest.object.Mean;

import com.google.inject.Inject;

/**
 * Implementation of {@link MeanServiceLocal}. This class's responsibility is to removed the fields
 * the subject does not have access to.
 * 
 * @author CLA
 * @since 0.14.0
 */
public class MeanServiceBean implements MeanServiceLocal {

    @Inject
    private MeanBusinessLocal meanBusiness;

    MeanServiceBean() {}

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public List<Mean> getMeans(final List<String> ids) {
        final List<Mean> meanList = new ArrayList<>();

        for (final String id : ids) {
            final Mean mean = meanBusiness.getMean(Long.valueOf(id));
            if (mean != null) {
                meanList.add(mean);
            }
        }

        return meanList;
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public Mean getMean(final String id) {
        if (id == null) {
            return null;
        } else {
            return meanBusiness.getMean(Long.valueOf(id));
        }
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public Mean saveMean(final Mean mean) {
        if (mean == null) {
            return null;
        } else {
            return meanBusiness.saveMean(mean);
        }
    }

}
