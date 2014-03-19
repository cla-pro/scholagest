package net.scholagest.app.rest.ws;

import java.util.ArrayList;
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
import net.scholagest.app.rest.ws.objects.BaseJson;
import net.scholagest.app.rest.ws.objects.BranchPeriod;
import net.scholagest.app.rest.ws.objects.ClazzJson;
import net.scholagest.app.rest.ws.objects.Period;
import net.scholagest.app.rest.ws.objects.Result;
import net.scholagest.app.rest.ws.objects.StudentResult;
import net.scholagest.object.Clazz;
import net.scholagest.service.ClazzServiceLocal;

import com.google.inject.Inject;

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

    @Inject
    public ClassesRest(final ClazzServiceLocal clazzService) {
        this.clazzService = clazzService;
    }

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

    @CheckAuthorization
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

    private Map<String, Object> updateData(final ClazzJson base, final ClazzJson toMerge) {
        final List<Object> updatedBranchPeriods = new ArrayList<>();
        final List<Object> updatedStudentResults = new ArrayList<>();
        final List<Object> updatedResults = new ArrayList<>();
        final List<Object> updatedMeans = new ArrayList<>();

        for (final String periodId : base.getPeriods()) {
            final Period period = PeriodsRest.periods.get(periodId);
            for (final String branchPeriodId : period.getBranchPeriods()) {
                final BranchPeriod branchPeriod = BranchPeriodsRest.branchPeriods.get(branchPeriodId);
                final List<String> studentResults = branchPeriod.getStudentResults();

                for (final String studentId : base.getStudents()) {
                    if (!toMerge.getStudents().contains(studentId)) {
                        final StudentResult studentResult = findStudentResultWithStudentId(studentResults, studentId);
                        studentResult.setActive(false);
                        updatedStudentResults.add(studentResult);
                    }
                }

                for (final String studentId : toMerge.getStudents()) {
                    if (!base.getStudents().contains(studentId)) {
                        final StudentResult studentResult = findStudentResultWithStudentId(studentResults, studentId);
                        if (studentResult != null) {
                            studentResult.setActive(true);
                            updatedStudentResults.add(studentResult);
                        } else {
                            final String studentResultId = IdHelper.getNextId(ExamsRest.studentResults.keySet());

                            // TODO create results and mean
                            final List<Result> newResults = createResults(studentResultId, branchPeriod.getExams());
                            final Result mean = createMean(studentResultId);

                            updatedResults.addAll(newResults);
                            updatedMeans.add(mean);

                            final StudentResult newStudentResult = new StudentResult(studentResultId, studentId, branchPeriodId,
                                    extractIds(newResults), mean.getId(), true);
                            ExamsRest.studentResults.put(studentResultId, newStudentResult);
                            updatedStudentResults.add(newStudentResult);

                            branchPeriod.getStudentResults().add(studentResultId);
                            if (!updatedBranchPeriods.contains(branchPeriod)) {
                                updatedBranchPeriods.add(branchPeriod);
                            }
                        }
                    }
                }

                for (final String studentResultId : branchPeriod.getStudentResults()) {
                    final StudentResult studentResult = ExamsRest.studentResults.get(studentResultId);
                    final String studentId = studentResult.getStudent();
                    if (base.getStudents().contains(studentId) && !toMerge.getStudents().contains(studentId)) {
                        studentResult.setActive(false);
                    } else if (!base.getStudents().contains(studentId) && toMerge.getStudents().contains(studentId)) {
                        studentResult.setActive(true);
                    }
                }
            }
        }

        final Map<String, Object> response = new HashMap<>();
        response.put("studentResults", updatedStudentResults);
        response.put("branchPeriods", updatedBranchPeriods);
        response.put("results", updatedResults);
        response.put("means", updatedMeans);

        return response;
    }

    private List<String> extractIds(final List<? extends BaseJson> elements) {
        final List<String> ids = new ArrayList<>();
        for (final BaseJson base : elements) {
            ids.add(base.getId());
        }
        return ids;
    }

    private List<Result> createResults(final String studentResultId, final List<String> exams) {
        final List<Result> results = new ArrayList<>();

        for (final String examId : exams) {
            final String resultId = IdHelper.getNextId(ResultsRest.results.keySet());
            final Result result = new Result(resultId, null, examId, studentResultId);
            results.add(result);
            ResultsRest.results.put(resultId, result);
        }

        return results;
    }

    private Result createMean(final String studentResultId) {
        final String meanId = IdHelper.getNextId(ResultsRest.results.keySet());
        final Result mean = new Result(meanId, null, null, studentResultId);
        ResultsRest.results.put(meanId, mean);
        return mean;
    }

    private StudentResult findStudentResultWithStudentId(final List<String> studentResultIds, final String studentId) {
        for (final String studentResultId : studentResultIds) {
            final StudentResult studentResult = ExamsRest.studentResults.get(studentResultId);
            if (studentResult.equals(studentId)) {
                return studentResult;
            }
        }

        return null;
    }

    @CheckAuthorization
    @POST
    public Map<String, Object> createClass(final Map<String, ClazzJson> payload) {
        final ClazzJsonConverter converter = new ClazzJsonConverter();

        final ClazzJson clazzJson = payload.get("class");
        final List<Period> periods = createPeriods(clazzJson);
        final Clazz clazz = converter.convertToClazz(clazzJson);

        final Clazz created = clazzService.createClazz(clazz);
        final ClazzJson createdJson = converter.convertToClazzJson(created);

        final Map<String, Object> response = new HashMap<>();
        response.put("class", createdJson);
        response.put("periods", periods);

        return response;
    }

    private List<Period> createPeriods(final ClazzJson clazz) {
        final List<Period> periods = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            final String periodId = IdHelper.getNextId(PeriodsRest.periods.keySet());
            final Period period = new Period(periodId, "Trimestre " + (i + 1), clazz.getId(), new ArrayList<String>());
            PeriodsRest.periods.put(periodId, period);
            periods.add(period);
            clazz.getPeriods().add(periodId);
        }

        return periods;
    }
}
