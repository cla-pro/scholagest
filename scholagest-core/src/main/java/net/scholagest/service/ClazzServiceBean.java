package net.scholagest.service;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.authorization.RolesAndPermissions;
import net.scholagest.business.ClazzBusinessLocal;
import net.scholagest.object.Clazz;

import com.google.inject.Inject;

/**
 * Implementation of {@link ClazzServiceLocal}. This class's responsibility is to removed the fields
 * the subject does not have access to.
 * 
 * @author CLA
 * @since 0.14.0
 */
public class ClazzServiceBean implements ClazzServiceLocal {

    @Inject
    private ClazzBusinessLocal clazzBusiness;

    ClazzServiceBean() {}

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public List<Clazz> getClasses(final List<String> ids) {
        // TODO filter fields
        final List<Clazz> classes = new ArrayList<>();

        for (final String id : ids) {
            final Clazz clazz = clazzBusiness.getClazz(id);
            if (clazz != null) {
                classes.add(clazz);
            }
        }

        return classes;
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public Clazz getClazz(final String id) {
        // TODO filter fields
        if (id == null) {
            return null;
        }

        return clazzBusiness.getClazz(id);
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public Clazz createClazz(final Clazz clazz) {
        if (clazz == null) {
            return null;
        }

        return clazzBusiness.createClazz(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public Clazz saveClazz(final Clazz clazz) {
        if (clazz == null) {
            return null;
        }

        return clazzBusiness.saveClazz(clazz);
    }

}
