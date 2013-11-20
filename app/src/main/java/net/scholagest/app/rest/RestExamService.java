package net.scholagest.app.rest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import net.scholagest.app.rest.object.RestGetObjectSubListRequest;
import net.scholagest.app.rest.object.RestObject;
import net.scholagest.app.rest.object.create.RestCreateExamRequest;
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

    @POST
    @Path("/create")
    @Produces("text/json")
    public String createExam(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestCreateExamRequest request = new Gson().fromJson(content, RestCreateExamRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Map<String, Object> classInfo = JerseyHelper.listToMap(request.getKeys(), new ArrayList<Object>(request.getValues()));

            BaseObject exam = examService.createExam(request.getYearKey(), request.getClassKey(), request.getBranchKey(), request.getPeriodKey(),
                    classInfo);
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

    @POST
    @Path("/getExamsInfo")
    @Produces("text/json")
    public String getExamsInfo(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestGetObjectSubListRequest request = new Gson().fromJson(content, RestGetObjectSubListRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Set<BaseObject> teachers = new HashSet<>();
            for (String teacherKey : request.getKeys()) {
                BaseObject exam = examService.getExamProperties(teacherKey, request.getProperties());

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
