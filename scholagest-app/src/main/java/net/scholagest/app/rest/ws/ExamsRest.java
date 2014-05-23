package net.scholagest.app.rest.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.converter.BranchPeriodJsonConverter;
import net.scholagest.app.rest.ws.converter.ExamJsonConverter;
import net.scholagest.app.rest.ws.converter.ResultJsonConverter;
import net.scholagest.app.rest.ws.converter.StudentResultJsonConverter;
import net.scholagest.app.rest.ws.objects.BranchPeriodJson;
import net.scholagest.app.rest.ws.objects.ExamJson;
import net.scholagest.app.rest.ws.objects.ResultJson;
import net.scholagest.app.rest.ws.objects.StudentResultJson;
import net.scholagest.object.BranchPeriod;
import net.scholagest.object.Exam;
import net.scholagest.object.Result;
import net.scholagest.object.StudentResult;
import net.scholagest.service.BranchPeriodServiceLocal;
import net.scholagest.service.ExamServiceLocal;
import net.scholagest.service.ResultServiceLocal;
import net.scholagest.service.StudentResultServiceLocal;

import com.google.inject.Inject;

/**
 * Set methods available for rest calls (WebService) to handle the exams (see {@link ExamJson}). The 
 * available methods are:
 * 
 * <ul>
 *   <li>PUT /{id} - to update the information of a exam</li>
 *   <li>POST - to create a new exam. The {@link ResultJson}s are created and the {StudentResultJson}s 
 *   and the {BranchPeriodJson} are updated as well</li>
 * </ul>
 * 
 * @author CLA
 * @since 0.14.0
 */
@Path("/exams")
public class ExamsRest {

    @Inject
    private ExamServiceLocal examService;

    @Inject
    private BranchPeriodServiceLocal branchPeriodService;

    @Inject
    private StudentResultServiceLocal studentResultService;

    @Inject
    private ResultServiceLocal resultService;

    ExamsRest() {}

    /**
     * Save the changes of the exam into the system.
     * 
     * @param id Id of the updated exam
     * @param payload Exam's information to save
     * @return The updated exam
     */
    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveExam(@PathParam("id") final String id, final Map<String, ExamJson> payload) {
        final ExamJsonConverter converter = new ExamJsonConverter();

        final ExamJson examJson = payload.get("exam");
        final Exam exam = converter.convertToExam(examJson);
        exam.setId(id);

        final Exam updated = examService.saveExam(id, exam);
        final ExamJson updatedJson = converter.convertToExamJson(updated);

        final Map<String, Object> response = new HashMap<>();
        response.put("exam", updatedJson);

        return ResponseUtils.build200OkResponse(response);
    }

    /**
     * Create a new exam. The {@link ResultJson}s are created and the {@link StudentResultJson} and 
     * {@link BranchPeriodJson} are updated within the same operation.
     * 
     * @param payload The exam's information to save on creation
     * @return The newly created exam with its {@link ResultJson}, {@link StudentResultJson} and {@link BranchPeriodJson
     */
    @CheckAuthorization
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createExam(final Map<String, ExamJson> payload) {
        final ExamJsonConverter converter = new ExamJsonConverter();

        final ExamJson examJson = payload.get("exam");
        final Exam exam = converter.convertToExam(examJson);

        final Exam created = examService.createExam(exam);
        final BranchPeriodJson branchPeriodJson = getBranchPeriodJson(created);
        final List<StudentResultJson> studentResultJsonList = getStudentResult(branchPeriodJson);
        final List<ResultJson> resultJsonList = getResultJsonList(studentResultJsonList, created.getId());

        final Map<String, Object> response = new HashMap<>();
        response.put("exam", converter.convertToExamJson(created));
        response.put("results", resultJsonList);
        response.put("studentResults", studentResultJsonList);
        response.put("branchPeriod", branchPeriodJson);

        return ResponseUtils.build200OkResponse(response);
    }

    private BranchPeriodJson getBranchPeriodJson(final Exam exam) {
        final BranchPeriodJsonConverter converter = new BranchPeriodJsonConverter();
        final BranchPeriod branchPeriod = branchPeriodService.getBranchPeriod(exam.getBranchPeriod());
        return converter.convertToBranchPeriodJson(branchPeriod);
    }

    private List<StudentResultJson> getStudentResult(final BranchPeriodJson branchPeriodJson) {
        final StudentResultJsonConverter converter = new StudentResultJsonConverter();
        final List<StudentResult> studentResultList = studentResultService.getStudentResults(branchPeriodJson.getStudentResults());
        return converter.convertToStudentResultJsonList(studentResultList);
    }

    private List<ResultJson> getResultJsonList(final List<StudentResultJson> studentResultJsonList, final String examId) {
        final ResultJsonConverter converter = new ResultJsonConverter();
        final List<ResultJson> resultJsonList = new ArrayList<>();

        for (final StudentResultJson studentResultJson : studentResultJsonList) {
            final List<Result> resultList = resultService.getResults(studentResultJson.getResults());
            final Result result = getResultForExam(resultList, examId);
            resultJsonList.add(converter.convertToResultJson(result));
        }

        return resultJsonList;
    }

    private Result getResultForExam(final List<Result> resultList, final String examId) {
        for (final Result result : resultList) {
            if (examId.equals(result.getExam())) {
                return result;
            }
        }
        return null;
    }
}
