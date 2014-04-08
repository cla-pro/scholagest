package net.scholagest.app.rest.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.converter.BranchJsonConverter;
import net.scholagest.app.rest.ws.converter.BranchPeriodJsonConverter;
import net.scholagest.app.rest.ws.converter.ExamJsonConverter;
import net.scholagest.app.rest.ws.converter.ResultJsonConverter;
import net.scholagest.app.rest.ws.converter.StudentResultJsonConverter;
import net.scholagest.app.rest.ws.objects.BranchJson;
import net.scholagest.app.rest.ws.objects.BranchPeriodJson;
import net.scholagest.app.rest.ws.objects.ExamJson;
import net.scholagest.app.rest.ws.objects.ResultJson;
import net.scholagest.app.rest.ws.objects.StudentResultJson;
import net.scholagest.object.Branch;
import net.scholagest.object.BranchPeriod;
import net.scholagest.object.Exam;
import net.scholagest.object.Result;
import net.scholagest.object.StudentResult;
import net.scholagest.service.BranchPeriodServiceLocal;
import net.scholagest.service.BranchServiceLocal;
import net.scholagest.service.ExamServiceLocal;
import net.scholagest.service.ResultServiceLocal;
import net.scholagest.service.StudentResultServiceLocal;

import com.google.inject.Inject;

/**
 * Set methods available for rest calls (WebService) to handle the branch periods (see {@link BranchPeriodJson}). The 
 * available methods are:
 * 
 * <ul>
 *   <li>GET ids[] - to retrieve a list of {@link BranchPeriodJson} filtered by the ids</li>
 *   <li>GET /{id} - to retrieve the information of a {@link BranchPeriodJson}</li>
 * </ul>
 * 
 * Both methods gives the {@link BranchJson}, {@link ExamJson}s, {@link StudentResultJson}s, {@link ResultJson} and the means 
 * as {@link ResultJson} back as well.
 * 
 * @author CLA
 * @since 0.14.0
 */
@Path("/branchPeriods")
public class BranchPeriodsRest {

    @Inject
    private BranchPeriodServiceLocal branchPeriodService;

    @Inject
    private BranchServiceLocal branchService;

    @Inject
    private ExamServiceLocal examService;

    @Inject
    private ResultServiceLocal resultService;

    @Inject
    private StudentResultServiceLocal studentResultService;

    BranchPeriodsRest() {}

    /**
     * Retrieve the information about a single {@link BranchPeriodJson} identified by its id.
     * 
     * @param id Id of the branch period to get
     * @return The branch period identified by id
     */
    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getBranchPeriod(@PathParam("id") final String id) {
        return getBranchPeriodInformation(Arrays.asList(id));
    }

    /**
     * <p>
     * Retrieve a list of {@link BranchPeriodJson}s filtered by ids. The ids are specified as query parameter with the name "ids[]".
     * </p>
     * 
     * <p>
     * Examples:
     * <ul>
     *   <li>Filter the branches by id: GET base_url/branchPeriods?ids[]=1&ids[]=2</li>
     * </ul>
     * </p>
     * 
     * @param ids Parameter used to filter the list of branch periods
     * @return The list of branch periods filtered by ids
     */
    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getBranchPeriods(@QueryParam("ids[]") final List<String> ids) {
        return getBranchPeriodInformation(ids);
    }

    private Map<String, Object> getBranchPeriodInformation(final List<String> ids) {
        final List<BranchPeriodJson> branchPeriodJsonList = getBranchPeriodJsonList(ids);
        final List<BranchJson> branchJsonList = new ArrayList<>();
        final List<ExamJson> examJsonList = new ArrayList<>();
        final List<StudentResultJson> studentResultJsonList = new ArrayList<>();
        final List<ResultJson> resultJsonList = new ArrayList<>();
        final List<ResultJson> meanJsonList = new ArrayList<>();

        for (final BranchPeriodJson branchPeriodJson : branchPeriodJsonList) {
            branchJsonList.add(getBranchJson(branchPeriodJson));
            examJsonList.addAll(getExamJsonList(branchPeriodJson.getExams()));

            final List<StudentResultJson> studentResultJsons = getStudentResultJsonList(branchPeriodJson.getStudentResults());
            studentResultJsonList.addAll(studentResultJsons);

            resultJsonList.addAll(getResultJsonList(studentResultJsons));
            meanJsonList.addAll(getMeanJsonList(studentResultJsons));
        }

        final Map<String, Object> response = new HashMap<>();
        response.put("branches", branchJsonList);
        response.put("branchPeriods", branchPeriodJsonList);
        response.put("exams", examJsonList);
        response.put("studentResults", studentResultJsonList);
        response.put("results", resultJsonList);
        response.put("means", meanJsonList);

        return response;
    }

    private List<BranchPeriodJson> getBranchPeriodJsonList(final List<String> ids) {
        final BranchPeriodJsonConverter converter = new BranchPeriodJsonConverter();

        final List<BranchPeriod> branchPeriodList = branchPeriodService.getBranchPeriods(ids);
        return converter.convertToBranchPeriodJsonList(branchPeriodList);
    }

    private BranchJson getBranchJson(final BranchPeriodJson branchPeriodJson) {
        final BranchJsonConverter converter = new BranchJsonConverter();

        final Branch branch = branchService.getBranch(branchPeriodJson.getBranch());
        final BranchJson branchJson = converter.convertToBranchJson(branch);

        return branchJson;
    }

    private List<ExamJson> getExamJsonList(final List<String> examIdList) {
        final ExamJsonConverter converter = new ExamJsonConverter();

        final List<Exam> examList = examService.getExams(examIdList);
        return converter.convertToExamJsonList(examList);
    }

    private List<StudentResultJson> getStudentResultJsonList(final List<String> studentResultIdList) {
        final StudentResultJsonConverter converter = new StudentResultJsonConverter();

        final List<StudentResult> studentResultList = studentResultService.getStudentResults(studentResultIdList);
        return converter.convertToStudentResultJsonList(studentResultList);
    }

    private List<ResultJson> getResultJsonList(final List<StudentResultJson> studentResultJsonList) {
        final ResultJsonConverter converter = new ResultJsonConverter();

        final List<Result> resultList = new ArrayList<>();
        for (final StudentResultJson studentResultJson : studentResultJsonList) {
            resultList.addAll(resultService.getResults(studentResultJson.getResults()));
        }

        return converter.convertToResultJsonList(resultList);
    }

    private List<ResultJson> getMeanJsonList(final List<StudentResultJson> studentResultJsonList) {
        final ResultJsonConverter converter = new ResultJsonConverter();

        final List<Result> meanList = new ArrayList<>();
        for (final StudentResultJson studentResultJson : studentResultJsonList) {
            meanList.add(resultService.getResult(studentResultJson.getMean()));
        }

        return converter.convertToResultJsonList(meanList);
    }

    // @CheckAuthorization
    // @Path("/{id}")
    // @PUT
    // public void saveBranchPeriod(@PathParam("id") final String id, final
    // Map<String, BranchPeriodJson> payload) {
    // final BranchPeriodJsonConverter converter = new
    // BranchPeriodJsonConverter();
    //
    // final BranchPeriodJson branchPeriod = payload.get("branchPeriod");
    // mergeBranchPeriod(branchPeriods.get(id), branchPeriod);
    // }
    //
    // private void mergeBranchPeriod(final BranchPeriodJson base, final
    // BranchPeriodJson toMerge) {
    // base.setExams(toMerge.getExams());
    // base.setStudentResults(toMerge.getStudentResults());
    // }

    // private List<ResultJson> meansForStudentResults(final
    // List<StudentResultJson> studentResultsForExams) {
    // final List<ResultJson> means = new ArrayList<>();
    //
    // for (final StudentResultJson studentResult : studentResultsForExams) {
    // means.add(ResultsRest.results.get(studentResult.getMean()));
    // }
    //
    // return means;
    // }
    //
    // private List<ResultJson> resultsForStudentResults(final
    // List<StudentResultJson> studentResultsForExams) {
    // final List<ResultJson> results = new ArrayList<>();
    //
    // for (final StudentResultJson studentResult : studentResultsForExams) {
    // for (final String resultId : studentResult.getResults()) {
    // results.add(ResultsRest.results.get(resultId));
    // }
    // }
    //
    // return results;
    // }
    //
    // private List<StudentResultJson> studentResultsForBranchPeriods(final
    // List<BranchPeriodJson> branchPeriods) {
    // final List<StudentResultJson> studentResults = new ArrayList<>();
    //
    // for (final BranchPeriodJson branchPeriod : branchPeriods) {
    // for (final String resultId : branchPeriod.getStudentResults()) {
    // studentResults.add(ExamsRest.studentResults.get(resultId));
    // }
    // }
    //
    // return studentResults;
    // }
    //
    // private List<ExamJson> examsForBranchPeriods(final List<BranchPeriodJson>
    // branchPeriods) {
    // final List<ExamJson> exams = new ArrayList<>();
    //
    // for (final BranchPeriodJson branchPeriod : branchPeriods) {
    // for (final String examId : branchPeriod.getExams()) {
    // exams.add(ExamsRest.exams.get(examId));
    // }
    // }
    //
    // return exams;
    // }
    //
    // private List<BranchJson> branchesForBranchPeriods(final
    // List<BranchPeriodJson> branchPeriods) {
    // final List<BranchJson> branches = new ArrayList<>();
    //
    // for (final BranchPeriodJson branchPeriod : branchPeriods) {
    // branches.add(BranchesRest.branches.get(branchPeriod.getBranch()));
    // }
    //
    // return branches;
    // }
    //
    // private List<BranchPeriodJson> branchPeriodsWithIds(final List<String>
    // ids) {
    // final ArrayList<BranchPeriodJson> filtered = new ArrayList<>();
    //
    // for (final String id : ids) {
    // filtered.add(branchPeriods.get(id));
    // }
    //
    // return filtered;
    // }
}
