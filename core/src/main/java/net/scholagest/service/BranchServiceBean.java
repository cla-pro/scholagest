package net.scholagest.service;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.authorization.Permission;
import net.scholagest.authorization.RolesAndPermissions;
import net.scholagest.business.BranchBusinessLocal;
import net.scholagest.object.Branch;

import com.google.inject.Inject;

/**
 * Implementation of {@link BranchServiceLocal}. This class's responsibility is to removed the fields
 * the subject does not have access to.
 * 
 * @author CLA
 * @since 0.14.0
 */
public class BranchServiceBean implements BranchServiceLocal {
    private final BranchBusinessLocal branchBusiness;

    @Inject
    public BranchServiceBean(final BranchBusinessLocal branchBusiness) {
        this.branchBusiness = branchBusiness;
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public List<Branch> getBranches(final List<String> ids) {
        final List<Branch> branches = new ArrayList<>();

        for (final String id : ids) {
            final Branch branch = branchBusiness.getBranch(id);
            if (branch != null) {
                branches.add(branch);
            }
        }

        return branches;
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = {})
    @Override
    public Branch getBranch(final String id) {
        if (id == null) {
            return null;
        } else {
            return branchBusiness.getBranch(id);
        }
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public Branch createBranch(final Branch branch) {
        if (branch == null) {
            return null;
        } else {
            return branchBusiness.createBranch(branch);
        }
    }

    /**
     * {@inheritDoc}
     */
    @RolesAndPermissions(roles = { "ADMIN" })
    @Override
    public Branch saveBranch(@Permission final String id, final Branch branch) {
        if (id == null || branch == null) {
            return null;
        } else {
            return branchBusiness.saveBranch(branch);
        }
    }

}
