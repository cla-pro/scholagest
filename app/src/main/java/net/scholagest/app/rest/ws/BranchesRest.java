package net.scholagest.app.rest.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.objects.Branch;
import net.scholagest.app.rest.ws.objects.BranchPeriod;
import net.scholagest.app.rest.ws.objects.Period;

import com.google.inject.Inject;

@Path("/branches")
public class BranchesRest {
    public static Map<String, Branch> branches = new HashMap<>();

    static {
        branches.put("1", new Branch("1", "Math", true, "1", Arrays.asList("1", "3", "5")));
        branches.put("2", new Branch("2", "Histoire", false, "1", Arrays.asList("2", "4")));
    }

    @Inject
    public BranchesRest() {}

    @CheckAuthorization
    @Path("/{id}")
    @GET
    public Map<String, Branch> getBranch(@PathParam("id") final String id) {
        final Map<String, Branch> result = new HashMap<String, Branch>();
        final Branch branch = branches.get(id);
        result.put("branch", branch);
        return result;
    }

    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getBranches(@QueryParam("ids[]") final List<String> ids) {
        final List<Branch> branchList = new ArrayList<>();

        for (final String branchId : ids) {
            branchList.add(branches.get(branchId));
        }

        final Map<String, Object> response = new HashMap<>();
        response.put("branches", branchList);

        return response;
    }

    @CheckAuthorization
    @Path("/{id}")
    @PUT
    public void saveBranch(@PathParam("id") final String id, final Map<String, Branch> payload) {
        final Branch branch = payload.get("branch");
        mergeBranch(branches.get(id), branch);
    }

    private void mergeBranch(final Branch base, final Branch toMerge) {
        base.setName(toMerge.getName());
    }

    @CheckAuthorization
    @POST
    public Map<String, Object> createBranch(final Map<String, Branch> payload) {
        final Branch branch = payload.get("branch");
        final String id = IdHelper.getNextId(branches.keySet());
        branch.setId(id);

        // final List<BranchPeriod> newBranchPeriods =
        // createBranchPeriods(branch);
        // branch.setBranchPeriods(IdHelper.extractIds(newBranchPeriods));

        branches.put(id, branch);

        final Map<String, Object> response = new HashMap<>();
        response.put("branch", branch);
        // response.put("branchPeriods", new
        // ArrayList<Object>(newBranchPeriods));
        // response.put("periods", new
        // ArrayList<Object>(updatePeriods(newBranchPeriods)));

        return response;
    }

    private List<Period> updatePeriods(final List<BranchPeriod> newBranchPeriods) {
        final List<Period> updated = new ArrayList<>();
        for (final BranchPeriod branchPeriod : newBranchPeriods) {
            final Period period = PeriodsRest.periods.get(branchPeriod.getPeriod());
            period.getBranchPeriods().add(branchPeriod.getId());
            updated.add(period);
        }
        return updated;
    }

    // private List<BranchPeriod> createBranchPeriods(final Branch branch) {
    // final ClazzJson clazz = ClassesRest.classes.get(branch.getClazz());
    //
    // final List<BranchPeriod> newBranchPeriods = new ArrayList<>();
    // for (final String periodId : clazz.getPeriods()) {
    // final String id =
    // IdHelper.getNextId(BranchPeriodsRest.branchPeriods.keySet());
    // final BranchPeriod branchPeriod = new BranchPeriod(id, branch.getId(),
    // periodId, new ArrayList<String>(), new ArrayList<String>());
    // BranchPeriodsRest.branchPeriods.put(id, branchPeriod);
    // newBranchPeriods.add(branchPeriod);
    // }
    //
    // return newBranchPeriods;
    // }
}
