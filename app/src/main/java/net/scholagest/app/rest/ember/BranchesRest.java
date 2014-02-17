package net.scholagest.app.rest.ember;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.AbstractService;
import net.scholagest.app.rest.ember.authorization.CheckAuthorization;
import net.scholagest.app.rest.ember.objects.Branch;
import net.scholagest.app.rest.ember.objects.Exam;
import net.scholagest.app.rest.ember.objects.Result;
import net.scholagest.app.rest.ember.objects.StudentResult;
import net.scholagest.services.IOntologyService;

import com.google.inject.Inject;

@Path("/branches")
public class BranchesRest extends AbstractService {
    public static Map<String, Branch> branches = new HashMap<>();

    static {
        branches.put("1", new Branch("1", "Math", true, "1", Arrays.asList("1", "2"), Arrays.asList("1")));
        branches.put("2", new Branch("2", "Histoire", false, "1", Arrays.asList("3", "4", "5"), Arrays.asList("2")));
        branches.put("3", new Branch("3", "Math", true, "2", new ArrayList<String>(), new ArrayList<String>()));
        branches.put("4", new Branch("4", "Histoire", false, "2", new ArrayList<String>(), new ArrayList<String>()));
        branches.put("5", new Branch("5", "Math", true, "3", new ArrayList<String>(), new ArrayList<String>()));
    }

    @Inject
    public BranchesRest(IOntologyService ontologyService) {
        super(ontologyService);
    }

    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, List<Object>> getBranches(@QueryParam("ids[]") final List<String> ids) {
        Map<String, List<Object>> toReturn = new HashMap<>();

        final List<Branch> branchesWithIds = branchesWithIds(ids);
        final List<Exam> examsForBranches = examsForBranches(branchesWithIds);
        final List<StudentResult> studentResultsForExams = studentResultsForBranches(branchesWithIds);
        final List<Result> resultsForStudentResults = resultsForStudentResults(studentResultsForExams);
        final List<Result> meansForStudentResults = meansForStudentResults(studentResultsForExams);

        toReturn.put("branches", new ArrayList<Object>(branchesWithIds));
        toReturn.put("exams", new ArrayList<Object>(examsForBranches));
        toReturn.put("studentResults", new ArrayList<Object>(studentResultsForExams));
        toReturn.put("results", new ArrayList<Object>(resultsForStudentResults));
        toReturn.put("means", new ArrayList<Object>(meansForStudentResults));

        return toReturn;
    }

    private List<Result> meansForStudentResults(List<StudentResult> studentResultsForExams) {
        final List<Result> means = new ArrayList<>();

        for (StudentResult studentResult : studentResultsForExams) {
            means.add(ResultsRest.results.get(studentResult.getMean()));
        }

        return means;
    }

    private List<Result> resultsForStudentResults(List<StudentResult> studentResultsForExams) {
        final List<Result> results = new ArrayList<>();

        for (StudentResult studentResult : studentResultsForExams) {
            for (String resultId : studentResult.getResults()) {
                results.add(ResultsRest.results.get(resultId));
            }
        }

        return results;
    }

    private List<StudentResult> studentResultsForBranches(List<Branch> branchesWithIds) {
        final List<StudentResult> studentResults = new ArrayList<>();

        for (Branch branch : branchesWithIds) {
            for (String resultId : branch.getStudentResults()) {
                studentResults.add(ExamsRest.studentResult.get(resultId));
            }
        }

        return studentResults;
    }

    private List<Exam> examsForBranches(List<Branch> branchesWithIds) {
        final List<Exam> exams = new ArrayList<>();

        for (Branch branch : branchesWithIds) {
            for (String examId : branch.getExams()) {
                exams.add(ExamsRest.exams.get(examId));
            }
        }

        return exams;
    }

    private List<Branch> branchesWithIds(List<String> ids) {
        ArrayList<Branch> filtered = new ArrayList<>();

        for (String id : ids) {
            filtered.add(branches.get(id));
        }

        return filtered;
    }
}
