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
import net.scholagest.app.rest.ws.converter.ClazzJsonConverter;
import net.scholagest.app.rest.ws.converter.PeriodJsonConverter;
import net.scholagest.app.rest.ws.objects.ClazzJson;
import net.scholagest.app.rest.ws.objects.PeriodJson;
import net.scholagest.object.Clazz;
import net.scholagest.object.Period;
import net.scholagest.service.ClazzServiceLocal;
import net.scholagest.service.PeriodServiceLocal;

import com.google.inject.Inject;

/**
 * Set methods available for rest calls (WebService) to handle the classes (see {@link ClazzJson}). The 
 * available methods are:
 * 
 * <ul>
 *   <li>GET ids[] - to retrieve a list of classes filtered by the ids</li>
 *   <li>GET /{id} - to retrieve the information of a class</li>
 *   <li>PUT /{id} - to update the information of a class</li>
 *   <li>POST - to create a new class. The periods (see {@link PeriodJson}) are created as well</li>
 * </ul>
 * 
 * @author CLA
 * @since 0.14.0
 */
@Path("/classes")
public class ClassesRest {
    // public static Map<String, ClazzJson> classes = new HashMap<>();
    //
    // static {
    // classes.put("1", new ClazzJson("1", "1P A", "1", Arrays.asList("1", "2",
    // "3"), Arrays.asList("1"), Arrays.asList("1"), Arrays.asList("1", "2")));
    // classes.put("2", new ClazzJson("2", "2P A", "2", new ArrayList<String>(),
    // Arrays.asList("2"), Arrays.asList("2"), new ArrayList<String>()));
    // classes.put("3", new ClazzJson("3", "5P A", "2", new ArrayList<String>(),
    // new ArrayList<String>(), new ArrayList<String>(),
    // new ArrayList<String>()));
    // }

    private final ClazzServiceLocal clazzService;

    private final PeriodServiceLocal periodService;

    @Inject
    public ClassesRest(final ClazzServiceLocal clazzService, final PeriodServiceLocal periodService) {
        this.clazzService = clazzService;
        this.periodService = periodService;
    }

    /**
     * <p>
     * Retrieve a list of {@link ClazzJson} filtered by ids. The ids are specified as query parameter with the name "ids[]".
     * </p>
     * 
     * <p>
     * Examples:
     * <ul>
     *   <li>Filter the classes by id: GET base_url/classes?ids[]=1&ids[]=2</li>
     * </ul>
     * </p>
     * 
     * @param ids Parameter used to filter the list of classes
     * @return The list of classes filtered by ids
     */
    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getClasses(@QueryParam("ids[]") final List<String> ids) {
        final ClazzJsonConverter converter = new ClazzJsonConverter();

        final List<Clazz> classList = clazzService.getClasses(ids);
        final List<ClazzJson> clazzJsonList = converter.convertToClazzJsonList(classList);

        final Map<String, Object> toReturn = new HashMap<>();
        toReturn.put("classes", clazzJsonList);

        return toReturn;
    }

    /**
     * Retrieve the information about a single {@link ClazzJson} identified by its id.
     * 
     * @param id Id of the class to get
     * @return The class identified by id
     */

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getClass(@PathParam("id") final String id) {
        final ClazzJsonConverter converter = new ClazzJsonConverter();

        final Clazz clazz = clazzService.getClazz(id);
        final ClazzJson clazzJson = converter.convertToClazzJson(clazz);

        final Map<String, Object> toReturn = new HashMap<>();
        toReturn.put("class", clazzJson);

        return toReturn;
    }

    /**
     * Save the changes of the class into the system.
     * 
     * @param id Id of the updated class
     * @param payload Class's information to save
     * @return The updated class
     */
    @CheckAuthorization
    @PUT
    @Path("/{id}")
    public Map<String, Object> saveClass(@PathParam("id") final String id, final Map<String, ClazzJson> payload) {
        final ClazzJsonConverter converter = new ClazzJsonConverter();

        final ClazzJson clazzJson = payload.get("class");
        clazzJson.setId(id);
        final Clazz clazz = converter.convertToClazz(clazzJson);

        final Clazz updated = clazzService.saveClazz(clazz);
        final ClazzJson updatedJson = converter.convertToClazzJson(updated);

        // final Map<String, Object> updatedObjects = updateData(base, clazz);

        final Map<String, Object> response = new HashMap<>();
        response.put("class", updatedJson);
        // response.putAll(updatedObjects);

        return response;
    }

    // private Map<String, Object> updateData(final ClazzJson base, final
    // ClazzJson toMerge) {
    // final List<Object> updatedBranchPeriods = new ArrayList<>();
    // final List<Object> updatedStudentResults = new ArrayList<>();
    // final List<Object> updatedResults = new ArrayList<>();
    // final List<Object> updatedMeans = new ArrayList<>();
    //
    // for (final String periodId : base.getPeriods()) {
    // final PeriodJson period = PeriodsRest.periods.get(periodId);
    // for (final String branchPeriodId : period.getBranchPeriods()) {
    // final BranchPeriod branchPeriod =
    // BranchPeriodsRest.branchPeriods.get(branchPeriodId);
    // final List<String> studentResults = branchPeriod.getStudentResults();
    //
    // for (final String studentId : base.getStudents()) {
    // if (!toMerge.getStudents().contains(studentId)) {
    // final StudentResult studentResult =
    // findStudentResultWithStudentId(studentResults, studentId);
    // studentResult.setActive(false);
    // updatedStudentResults.add(studentResult);
    // }
    // }
    //
    // for (final String studentId : toMerge.getStudents()) {
    // if (!base.getStudents().contains(studentId)) {
    // final StudentResult studentResult =
    // findStudentResultWithStudentId(studentResults, studentId);
    // if (studentResult != null) {
    // studentResult.setActive(true);
    // updatedStudentResults.add(studentResult);
    // } else {
    // final String studentResultId =
    // IdHelper.getNextId(ExamsRest.studentResults.keySet());
    //
    // // TODO create results and mean
    // final List<Result> newResults = createResults(studentResultId,
    // branchPeriod.getExams());
    // final Result mean = createMean(studentResultId);
    //
    // updatedResults.addAll(newResults);
    // updatedMeans.add(mean);
    //
    // final StudentResult newStudentResult = new StudentResult(studentResultId,
    // studentId, branchPeriodId,
    // extractIds(newResults), mean.getId(), true);
    // ExamsRest.studentResults.put(studentResultId, newStudentResult);
    // updatedStudentResults.add(newStudentResult);
    //
    // branchPeriod.getStudentResults().add(studentResultId);
    // if (!updatedBranchPeriods.contains(branchPeriod)) {
    // updatedBranchPeriods.add(branchPeriod);
    // }
    // }
    // }
    // }
    //
    // for (final String studentResultId : branchPeriod.getStudentResults()) {
    // final StudentResult studentResult =
    // ExamsRest.studentResults.get(studentResultId);
    // final String studentId = studentResult.getStudent();
    // if (base.getStudents().contains(studentId) &&
    // !toMerge.getStudents().contains(studentId)) {
    // studentResult.setActive(false);
    // } else if (!base.getStudents().contains(studentId) &&
    // toMerge.getStudents().contains(studentId)) {
    // studentResult.setActive(true);
    // }
    // }
    // }
    // }
    //
    // final Map<String, Object> response = new HashMap<>();
    // response.put("studentResults", updatedStudentResults);
    // response.put("branchPeriods", updatedBranchPeriods);
    // response.put("results", updatedResults);
    // response.put("means", updatedMeans);
    //
    // return response;
    // }

    // private List<String> extractIds(final List<? extends BaseJson> elements)
    // {
    // final List<String> ids = new ArrayList<>();
    // for (final BaseJson base : elements) {
    // ids.add(base.getId());
    // }
    // return ids;
    // }
    //
    // private List<ResultJson> createResults(final String studentResultId,
    // final List<String> exams) {
    // final List<ResultJson> results = new ArrayList<>();
    //
    // for (final String examId : exams) {
    // final String resultId = IdHelper.getNextId(ResultsRest.results.keySet());
    // final ResultJson result = new ResultJson(resultId, null, examId,
    // studentResultId);
    // results.add(result);
    // ResultsRest.results.put(resultId, result);
    // }
    //
    // return results;
    // }
    //
    // private ResultJson createMean(final String studentResultId) {
    // final String meanId = IdHelper.getNextId(ResultsRest.results.keySet());
    // final ResultJson mean = new ResultJson(meanId, null, null,
    // studentResultId);
    // ResultsRest.results.put(meanId, mean);
    // return mean;
    // }
    //
    // private StudentResultJson findStudentResultWithStudentId(final
    // List<String> studentResultIds, final String studentId) {
    // for (final String studentResultId : studentResultIds) {
    // final StudentResultJson studentResult =
    // ExamsRest.studentResults.get(studentResultId);
    // if (studentResult.equals(studentId)) {
    // return studentResult;
    // }
    // }
    //
    // return null;
    // }

    /**
     * Create a new class. The {@link PeriodJson}s are created within the same operation.
     * 
     * @param payload The class's information to save on creation
     * @return The newly created class with its {@link PeriodJson}
     */
    @CheckAuthorization
    @POST
    public Map<String, Object> createClass(final Map<String, ClazzJson> payload) {
        final ClazzJsonConverter clazzConverter = new ClazzJsonConverter();
        final PeriodJsonConverter periodConverter = new PeriodJsonConverter();

        final ClazzJson clazzJson = payload.get("class");
        final Clazz clazz = clazzConverter.convertToClazz(clazzJson);

        final Clazz created = clazzService.createClazz(clazz);
        final ClazzJson createdJson = clazzConverter.convertToClazzJson(created);

        final List<Period> periodList = periodService.getPeriods(created.getPeriods());
        final List<PeriodJson> periodJsonList = periodConverter.convertToPeriodJsonList(periodList);

        final Map<String, Object> response = new HashMap<>();
        response.put("class", createdJson);
        response.put("periods", periodJsonList);

        return response;
    }
}
