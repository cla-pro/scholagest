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
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.objects.BranchPeriod;
import net.scholagest.app.rest.ws.objects.Exam;
import net.scholagest.app.rest.ws.objects.Result;
import net.scholagest.app.rest.ws.objects.StudentResult;

import com.google.inject.Inject;

@Path("/exams")
public class ExamsRest {
    public static Map<String, Exam> exams = new HashMap<>();
    public static Map<String, StudentResult> studentResults = new HashMap<>();

    static {
        exams.put("1", new Exam("1", "Récitation 1", 5, "1"));
        exams.put("2", new Exam("2", "Récitation 2", 4, "1"));
        exams.put("6", new Exam("6", "Moyenne", 1, "1"));
        exams.put("3", new Exam("3", "Récitation 3", 3, "4"));
        exams.put("4", new Exam("4", "Récitation 4", 2, "4"));
        exams.put("5", new Exam("5", "Récitation 5", 1, "4"));
        exams.put("7", new Exam("7", "Moyenne", 1, "4"));

        studentResults.put("1", new StudentResult("1", "1", "1", Arrays.asList("1", "2"), "3", true));
        studentResults.put("2", new StudentResult("2", "1", "2", Arrays.asList("4", "5", "6"), "7", true));
    }

    @Inject
    public ExamsRest() {}

    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, List<Object>> getExams() {
        final Map<String, List<Object>> examsToReturn = new HashMap<>();

        return examsToReturn;
    }

    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void saveExam(@PathParam("id") final String id, final Map<String, Exam> payload) {
        final Exam exam = payload.get("exam");
        mergeExam(exams.get(id), exam);
    }

    private void mergeExam(final Exam base, final Exam toMerge) {
        base.setCoeff(toMerge.getCoeff());
        base.setName(toMerge.getName());
    }

    @CheckAuthorization
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> createExam(final Map<String, Exam> payload) {
        final Exam exam = payload.get("exam");
        final String id = IdHelper.getNextId(exams.keySet());
        exam.setId(id);
        exams.put(id, exam);

        // TODO Update branchPeriod
        final BranchPeriod branchPeriod = updateBranchPeriod(exam);
        // Create the results
        final List<Result> results = createResults(exam);
        // Update the studentResults with the new results
        final List<StudentResult> studentResults = updateStudentResults(results);

        final Map<String, Object> response = new HashMap<>();
        response.put("exam", exam);
        response.put("results", results);
        response.put("studentResults", studentResults);
        response.put("branchPeriod", branchPeriod);

        return response;
    }

    private BranchPeriod updateBranchPeriod(final Exam exam) {
        final BranchPeriod branchPeriod = BranchPeriodsRest.branchPeriods.get(exam.getBranchPeriod());

        branchPeriod.getExams().add(exam.getId());

        return branchPeriod;
    }

    private List<StudentResult> updateStudentResults(final List<Result> results) {
        final List<StudentResult> updated = new ArrayList<>();

        for (final Result result : results) {
            final StudentResult studentResult = studentResults.get(result.getStudentResult());
            studentResult.getResults().add(result.getId());
            updated.add(studentResult);
        }

        return updated;
    }

    private List<Result> createResults(final Exam exam) {
        final BranchPeriod branchPeriod = BranchPeriodsRest.branchPeriods.get(exam.getBranchPeriod());

        final List<Result> results = new ArrayList<Result>();

        for (final String studentResultId : branchPeriod.getStudentResults()) {
            final String resultId = IdHelper.getNextId(ResultsRest.results.keySet());
            final Result result = new Result(resultId, null, exam.getId(), studentResultId);
            ResultsRest.results.put(resultId, result);
            results.add(result);
        }

        return results;
    }
}
