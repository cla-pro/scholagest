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
 * Implementation of {@see UserServiceLocal}. This class's responsibility is to removed the fields
 * the subject does not have access to.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class UserServiceBean implements UserServiceLocal {
    private final UserBusinessLocal userBusiness;

    @Inject
    public UserServiceBean(final UserBusinessLocal userBusiness) {
        this.userBusiness = userBusiness;
    }

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
