package net.scholagest.app.rest.ws;

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
import net.scholagest.app.rest.ws.converter.BranchJsonConverter;
import net.scholagest.app.rest.ws.converter.BranchPeriodJsonConverter;
import net.scholagest.app.rest.ws.converter.PeriodJsonConverter;
import net.scholagest.app.rest.ws.objects.BranchJson;
import net.scholagest.app.rest.ws.objects.BranchPeriodJson;
import net.scholagest.app.rest.ws.objects.PeriodJson;
import net.scholagest.app.rest.ws.objects.ResultJson;
import net.scholagest.app.rest.ws.objects.StudentResultJson;
import net.scholagest.object.Branch;
import net.scholagest.object.BranchPeriod;
import net.scholagest.object.Clazz;
import net.scholagest.object.Period;
import net.scholagest.object.StudentResult;
import net.scholagest.service.BranchPeriodServiceLocal;
import net.scholagest.service.BranchServiceLocal;
import net.scholagest.service.ClazzServiceLocal;
import net.scholagest.service.PeriodServiceLocal;

import com.google.inject.Inject;

/**
 * Set methods available for rest calls (WebService) to handle the branches (see {@link BranchJson}). The 
 * available methods are:
 * 
 * <ul>
 *   <li>GET ids[] - to retrieve a list of branches filtered by the ids</li>
 *   <li>GET /{id} - to retrieve the information of a branch</li>
 *   <li>PUT /{id} - to update the information of a branch</li>
 *   <li>POST - to create a new branch. Some other objects such as the {@link BranchPeriodJson}s,
 *   the {@link StudentResult}s and the {@link Result}s are created as well</li>
 * </ul>
 * 
 * @author CLA
 * @since 0.14.0
 */
@Path("/branches")
public class BranchesRest {
    // public static Map<String, BranchJson> branches = new HashMap<>();
    //
    // static {
    // branches.put("1", new BranchJson("1", "Math", true, "1",
    // Arrays.asList("1", "3", "5")));
    // branches.put("2", new BranchJson("2", "Histoire", false, "1",
    // Arrays.asList("2", "4")));
    // }

    private final BranchServiceLocal branchService;

    private final PeriodServiceLocal periodService;

    private final BranchPeriodServiceLocal branchPeriodService;

    private final ClazzServiceLocal clazzService;

    @Inject
    public BranchesRest(final BranchServiceLocal branchService, final PeriodServiceLocal periodService,
            final BranchPeriodServiceLocal branchPeriodService, final ClazzServiceLocal clazzService) {
        this.branchService = branchService;
        this.periodService = periodService;
        this.branchPeriodService = branchPeriodService;
        this.clazzService = clazzService;
    }

    /**
     * Retrieve the information about a single branch identified by its id.
     * 
     * @param id Id of the branch to get
     * @return The branch identified by id
     */
    @CheckAuthorization
    @Path("/{id}")
    @GET
    public Map<String, Object> getBranch(@PathParam("id") final String id) {
        final BranchJsonConverter converter = new BranchJsonConverter();

        final Branch branch = branchService.getBranch(id);
        final BranchJson branchJson = converter.convertToBranchJson(branch);

        final Map<String, Object> response = new HashMap<>();
        response.put("branch", branchJson);

        return response;
    }

    /**
     * <p>
     * Retrieve a list of {@link Branch}es filtered by ids. The ids are specified as query parameter with the name "ids[]".
     * </p>
     * 
     * <p>
     * Examples:
     * <ul>
     *   <li>Filter the branches by id: GET base_url/branches?ids[]=1&ids[]=2</li>
     * </ul>
     * </p>
     * 
     * @param ids Parameter used to filter the list of branches
     * @return The list of branches filtered by ids
     */
    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getBranches(@QueryParam("ids[]") final List<String> ids) {
        final BranchJsonConverter converter = new BranchJsonConverter();

        final List<Branch> branchList = branchService.getBranches(ids);
        final List<BranchJson> branchJsonList = converter.convertToBranchJsonList(branchList);

        final Map<String, Object> response = new HashMap<>();
        response.put("branches", branchJsonList);

        return response;
    }

    /**
     * Save the changes of the branch into the system.
     * 
     * @param id Id of the updated branch
     * @param payload Branch's information to save
     * @return The updated branch
     */
    @CheckAuthorization
    @Path("/{id}")
    @PUT
    public Map<String, Object> saveBranch(@PathParam("id") final String id, final Map<String, BranchJson> payload) {
        final BranchJsonConverter converter = new BranchJsonConverter();

        final BranchJson branchJson = payload.get("branch");
        final Branch branch = converter.convertToBranch(branchJson);
        branch.setId(id);

        final Branch saved = branchService.saveBranch(id, branch);
        final BranchJson savedJson = converter.convertToBranchJson(saved);

        final Map<String, Object> response = new HashMap<>();
        response.put("branch", savedJson);

        return response;
    }

    /**
     * Create a new branch. The {@link BranchPeriodJson}, {@link StudentResultJson} and {@link ResultJson} are created within the same operation.
     * 
     * @param payload The branch's information to save on creation
     * @return The newly created branch with its {@link BranchPeriodJson}, {@link StudentResultJson} and {@link ResultJson}
     */
    @CheckAuthorization
    @POST
    public Map<String, Object> createBranch(final Map<String, BranchJson> payload) {
        final BranchJsonConverter converter = new BranchJsonConverter();

        final BranchJson branchJson = payload.get("branch");
        final Branch branch = converter.convertToBranch(branchJson);

        final Branch created = branchService.createBranch(branch);
        final BranchJson createdJson = converter.convertToBranchJson(created);

        final List<PeriodJson> periodJsonList = getPeriods(created);
        final List<BranchPeriodJson> branchPeriodList = getBranchPeriods(created);

        final Map<String, Object> response = new HashMap<>();
        response.put("branch", createdJson);
        response.put("branchPeriods", branchPeriodList);
        response.put("periods", periodJsonList);

        return response;
    }

    private List<BranchPeriodJson> getBranchPeriods(final Branch branch) {
        final BranchPeriodJsonConverter branchPeriodConverter = new BranchPeriodJsonConverter();
        final List<BranchPeriod> branchPeriodList = branchPeriodService.getBranchPeriods(branch.getBranchPeriods());

        return branchPeriodConverter.convertToBranchPeriodJsonList(branchPeriodList);
    }

    private List<PeriodJson> getPeriods(final Branch branch) {
        final PeriodJsonConverter periodConverter = new PeriodJsonConverter();
        final Clazz clazz = clazzService.getClazz(branch.getClazz());
        final List<Period> periodList = periodService.getPeriods(clazz.getPeriods());

        return periodConverter.convertToPeriodJsonList(periodList);
    }

    // private List<PeriodJson> updatePeriods(final List<BranchPeriod>
    // newBranchPeriods) {
    // final List<PeriodJson> updated = new ArrayList<>();
    // for (final BranchPeriod branchPeriod : newBranchPeriods) {
    // final PeriodJson period =
    // PeriodsRest.periods.get(branchPeriod.getPeriod());
    // period.getBranchPeriods().add(branchPeriod.getId());
    // updated.add(period);
    // }
    // return updated;
    // }

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
