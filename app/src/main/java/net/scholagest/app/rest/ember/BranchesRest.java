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
        branches.put("1", new Branch("1", "Math", "1", Arrays.asList("1", "2"), Arrays.asList("1")));
        branches.put("2", new Branch("2", "Histoire", "1", new ArrayList<String>(), new ArrayList<String>()));
        branches.put("3", new Branch("3", "Math", "2", new ArrayList<String>(), new ArrayList<String>()));
        branches.put("4", new Branch("4", "Histoire", "2", Arrays.asList("3", "4", "5"), new ArrayList<String>()));
        branches.put("5", new Branch("5", "Math", "3", new ArrayList<String>(), new ArrayList<String>()));
    }

    @Inject
    public BranchesRest(IOntologyService ontologyService) {
        super(ontologyService);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, List<? extends Object>> getBranches(@QueryParam("ids[]") final List<String> ids) {
        Map<String, List<? extends Object>> toReturn = new HashMap<>();

        final List<Branch> branchesWithIds = branchesWithIds(ids);
        final List<Exam> examsForBranches = examsForBranches(branchesWithIds);
        final List<StudentResult> studentResultsForExams = studentResultsForBranches(branchesWithIds);
        final List<Result> resultsForStudentResults = resultsForStudentResults(studentResultsForExams);

        toReturn.put("branches", branchesWithIds);
        toReturn.put("exams", examsForBranches);
        toReturn.put("studentResults", studentResultsForExams);
        toReturn.put("results", resultsForStudentResults);

        return toReturn;
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
