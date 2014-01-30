package net.scholagest.app.rest.ember;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.AbstractService;
import net.scholagest.app.rest.ember.objects.Branch;
import net.scholagest.services.IOntologyService;

import com.google.inject.Inject;

@Path("/branches")
public class BranchesRest extends AbstractService {
    public static Map<String, Branch> branches = new HashMap<>();

    static {
        branches.put("1", new Branch("1", "Math", "1", Arrays.asList("1", "2")));
        branches.put("2", new Branch("2", "Histoire", "1", new ArrayList<String>()));
        branches.put("3", new Branch("3", "Math", "2", new ArrayList<String>()));
        branches.put("4", new Branch("4", "Histoire", "2", Arrays.asList("3", "4", "5")));
        branches.put("5", new Branch("5", "Math", "3", new ArrayList<String>()));
    }

    @Inject
    public BranchesRest(IOntologyService ontologyService) {
        super(ontologyService);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Collection<Branch>> getBranches() {
        Map<String, Collection<Branch>> branchesToReturn = new HashMap<>();

        branchesToReturn.put("branches", branches.values());

        return branchesToReturn;
    }

}
