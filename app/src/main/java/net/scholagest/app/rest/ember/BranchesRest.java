package net.scholagest.app.rest.ember;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import net.scholagest.app.rest.AbstractService;
import net.scholagest.app.rest.ember.authorization.CheckAuthorization;
import net.scholagest.app.rest.ember.objects.Branch;
import net.scholagest.services.IOntologyService;

import com.google.inject.Inject;

@Path("/branches")
public class BranchesRest extends AbstractService {
    public static Map<String, Branch> branches = new HashMap<>();

    static {
        branches.put("1", new Branch("1", "Math", true, "1", Arrays.asList("1", "3", "5")));
        branches.put("2", new Branch("2", "Histoire", false, "1", Arrays.asList("2", "4")));
    }

    @Inject
    public BranchesRest(final IOntologyService ontologyService) {
        super(ontologyService);
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
}
