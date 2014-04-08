/**
 * 
 */
package net.scholagest.service;

import net.scholagest.authorization.Permission;
import net.scholagest.authorization.RolesAndPermissions;
import net.scholagest.business.UserBusinessLocal;
import net.scholagest.object.User;

import com.google.inject.Inject;

/**
 * Implementation of {@link UserServiceLocal}. This class's responsibility is to removed the fields
 * the subject does not have access to.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class UserServiceBean implements UserServiceLocal {

    @Inject
    private UserBusinessLocal userBusiness;

    UserServiceBean() {}

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public User getUser(@Permission final String id) {
        if (id == null) {
            return null;
        }

        return userBusiness.getUser(id);
    }
}
