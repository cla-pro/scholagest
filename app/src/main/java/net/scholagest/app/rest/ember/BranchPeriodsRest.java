package net.scholagest.app.rest.ember;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.AbstractService;
import net.scholagest.app.rest.ember.authorization.CheckAuthorization;
import net.scholagest.app.rest.ember.objects.Branch;
import net.scholagest.app.rest.ember.objects.BranchPeriod;
import net.scholagest.app.rest.ember.objects.Exam;
import net.scholagest.app.rest.ember.objects.Result;
import net.scholagest.app.rest.ember.objects.StudentResult;
import net.scholagest.services.IOntologyService;

import com.google.inject.Inject;

@Path("/branchPeriods")
public class BranchPeriodsRest extends AbstractService {
    public static Map<String, BranchPeriod> branchPeriods = new HashMap<>();

    static {
        branchPeriods.put("1", new BranchPeriod("1", "1", "1", Arrays.asList("1", "2"), Arrays.asList("1")));
        branchPeriods.put("2", new BranchPeriod("2", "2", "1", Arrays.asList("3", "4", "5"), Arrays.asList("2")));
        branchPeriods.put("3", new BranchPeriod("3", "1", "2", new ArrayList<String>(), new ArrayList<String>()));
        branchPeriods.put("4", new BranchPeriod("4", "2", "2", new ArrayList<String>(), new ArrayList<String>()));
        branchPeriods.put("5", new BranchPeriod("5", "1", "3", new ArrayList<String>(), new ArrayList<String>()));
    }

    @Inject
    public BranchPeriodsRest(final IOntologyService ontologyService) {
        super(ontologyService);
    }

    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getBranch(@PathParam("id") final String id) {
        final Map<String, Object> toReturn = new HashMap<>();

        final BranchPeriod branchPeriod = branchPeriods.get(id);
        final Branch branch = BranchesRest.branches.get(branchPeriod.getBranch());
        final List<Exam> examsForBranchPeriods = examsForBranchPeriods(Arrays.asList(branchPeriod));
        final List<StudentResult> studentResultsForBranchPeriods = studentResultsForBranchPeriods(Arrays.asList(branchPeriod));
        final List<Result> resultsForStudentResults = resultsForStudentResults(studentResultsForBranchPeriods);
        final List<Result> meansForStudentResults = meansForStudentResults(studentResultsForBranchPeriods);

        toReturn.put("branch", branch);
        toReturn.put("branchPeriod", branchPeriod);
        toReturn.put("exams", new ArrayList<Object>(examsForBranchPeriods));
        toReturn.put("studentResults", new ArrayList<Object>(studentResultsForBranchPeriods));
        toReturn.put("results", new ArrayList<Object>(resultsForStudentResults));
        toReturn.put("means", new ArrayList<Object>(meansForStudentResults));

        return toReturn;
    }

    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getBranches(@QueryParam("ids[]") final List<String> ids) {
        final Map<String, Object> toReturn = new HashMap<>();

        final List<BranchPeriod> branchPeriodsWithIds = branchPeriodsWithIds(ids);
        final List<Branch> branchesWithIds = branchesForBranchPeriods(branchPeriodsWithIds);
        final List<Exam> examsForBranchPeriods = examsForBranchPeriods(branchPeriodsWithIds);
        final List<StudentResult> studentResultsForBranchPeriods = studentResultsForBranchPeriods(branchPeriodsWithIds);
        final List<Result> resultsForStudentResults = resultsForStudentResults(studentResultsForBranchPeriods);
        final List<Result> meansForStudentResults = meansForStudentResults(studentResultsForBranchPeriods);

        toReturn.put("branches", new ArrayList<Object>(branchesWithIds));
        toReturn.put("branchPeriods", new ArrayList<Object>(branchPeriodsWithIds));
        toReturn.put("exams", new ArrayList<Object>(examsForBranchPeriods));
        toReturn.put("studentResults", new ArrayList<Object>(studentResultsForBranchPeriods));
        toReturn.put("results", new ArrayList<Object>(resultsForStudentResults));
        toReturn.put("means", new ArrayList<Object>(meansForStudentResults));

        return toReturn;
    }

    @CheckAuthorization
    @Path("/{id}")
    @PUT
    public void saveBranchPeriod(@PathParam("id") final String id, final Map<String, BranchPeriod> payload) {
        final BranchPeriod branchPeriod = payload.get("branchPeriod");
        mergeBranchPeriod(branchPeriods.get(id), branchPeriod);
    }

    private void mergeBranchPeriod(final BranchPeriod base, final BranchPeriod toMerge) {
        base.setExams(toMerge.getExams());
        base.setStudentResults(toMerge.getStudentResults());
    }

    private List<Result> meansForStudentResults(final List<StudentResult> studentResultsForExams) {
        final List<Result> means = new ArrayList<>();

        for (final StudentResult studentResult : studentResultsForExams) {
            means.add(ResultsRest.results.get(studentResult.getMean()));
        }

        return means;
    }

    private List<Result> resultsForStudentResults(final List<StudentResult> studentResultsForExams) {
        final List<Result> results = new ArrayList<>();

        for (final StudentResult studentResult : studentResultsForExams) {
            for (final String resultId : studentResult.getResults()) {
                results.add(ResultsRest.results.get(resultId));
            }
        }

        return results;
    }

    private List<StudentResult> studentResultsForBranchPeriods(final List<BranchPeriod> branchPeriods) {
        final List<StudentResult> studentResults = new ArrayList<>();

        for (final BranchPeriod branchPeriod : branchPeriods) {
            for (final String resultId : branchPeriod.getStudentResults()) {
                studentResults.add(ExamsRest.studentResults.get(resultId));
            }
        }

        return studentResults;
    }

    private List<Exam> examsForBranchPeriods(final List<BranchPeriod> branchPeriods) {
        final List<Exam> exams = new ArrayList<>();

        for (final BranchPeriod branchPeriod : branchPeriods) {
            for (final String examId : branchPeriod.getExams()) {
                exams.add(ExamsRest.exams.get(examId));
            }
        }

        return exams;
    }

    private List<Branch> branchesForBranchPeriods(final List<BranchPeriod> branchPeriods) {
        final List<Branch> branches = new ArrayList<>();

        for (final BranchPeriod branchPeriod : branchPeriods) {
            branches.add(BranchesRest.branches.get(branchPeriod.getBranch()));
        }

        return branches;
    }

    private List<BranchPeriod> branchPeriodsWithIds(final List<String> ids) {
        final ArrayList<BranchPeriod> filtered = new ArrayList<>();

        for (final String id : ids) {
            filtered.add(branchPeriods.get(id));
        }

        return filtered;
    }
}
