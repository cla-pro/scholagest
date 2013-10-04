package net.scholagest.app.rest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.scholagest.app.rest.object.RestObject;
import net.scholagest.app.utils.JerseyHelper;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IExamService;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.IUserService;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.shiro.ShiroException;

import com.google.gson.Gson;
import com.google.inject.Inject;

@Path("/exam")
public class RestExamService extends AbstractService {
    private final static String REQUEST_ID_PREFIX = "exam-";
    private final IExamService examService;
    private final IUserService userService;

    @Inject
    public RestExamService(IExamService examService, IOntologyService ontologyService, IUserService userService) {
        super(ontologyService);
        this.examService = examService;
        this.userService = userService;
    }

    @GET
    @Path("/create")
    @Produces("text/json")
    public String createExam(@QueryParam("token") String token, @QueryParam("yearKey") String yearKey, @QueryParam("classKey") String classKey,
            @QueryParam("branchKey") String branchKey, @QueryParam("periodKey") String periodKey, @QueryParam("keys") List<String> keys,
            @QueryParam("values") List<String> values) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(token));

            Map<String, Object> classInfo = JerseyHelper.listToMap(keys, new ArrayList<Object>(values));

            BaseObject exam = examService.createExam(yearKey, classKey, branchKey, periodKey, classInfo);
            RestObject restClass = new RestToKdomConverter().restObjectFromKdom(exam);

            String json = new Gson().toJson(restClass);
            return "{info: " + json + "}";
        } catch (ShiroException e) {
            return handleShiroException(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (ScholagestRuntimeException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/getExamsInfo")
    @Produces("text/json")
    public String getTeachersInfo(@QueryParam("token") String token, @QueryParam("exams") Set<String> examKeyList,
            @QueryParam("properties") Set<String> properties) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(token));

            Set<BaseObject> teachers = new HashSet<>();
            for (String teacherKey : examKeyList) {
                BaseObject exam = examService.getExamProperties(teacherKey, properties);

                teachers.add(exam);
            }

            List<RestObject> restTeachers = new RestToKdomConverter().restObjectsFromKdoms(teachers);

            String json = new Gson().toJson(restTeachers);
            return "{info: " + json + "}";
        } catch (ShiroException e) {
            return handleShiroException(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (ScholagestRuntimeException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }
    }
}
