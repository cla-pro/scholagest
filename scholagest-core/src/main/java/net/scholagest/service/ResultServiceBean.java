package net.scholagest.service;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.authorization.RolesAndPermissions;
import net.scholagest.business.ResultBusinessLocal;
import net.scholagest.object.Result;

import com.google.inject.Inject;

/**
 * Implementation of {@link ResultServiceLocal}. This class's responsibility is to removed the fields
 * the subject does not have access to.
 * 
 * @author CLA
 * @since 0.14.0
 */
public class ResultServiceBean implements ResultServiceLocal {

    @Inject
    private ResultBusinessLocal resultBusiness;

    ResultServiceBean() {}

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public List<Result> getResults(final List<String> ids) {
        final List<Result> resultList = new ArrayList<>();

        for (final String id : ids) {
            final Result result = resultBusiness.getResult(Long.valueOf(id));
            if (result != null) {
                resultList.add(result);
            }
        }

        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public Result getResult(final String id) {
        if (id == null) {
            return null;
        } else {
            return resultBusiness.getResult(Long.valueOf(id));
        }
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public Result saveResult(final Result result) {
        if (result == null) {
            return null;
        } else {
            return resultBusiness.saveResult(result);
        }
    }

}
