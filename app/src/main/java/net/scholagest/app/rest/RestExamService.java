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
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IExamService;
import net.scholagest.services.IOntologyService;

import com.google.gson.Gson;
import com.google.inject.Inject;

@Path("/exam")
public class RestExamService extends AbstractService {
    private final static String REQUEST_ID_PREFIX = "exam-";
    private final IExamService examService;

    @Inject
    public RestExamService(IExamService examService, IOntologyService ontologyService) {
        super(ontologyService);
        this.examService = examService;
    }

    @GET
    @Path("/create")
    @Produces("text/json")
    public String createExam(@QueryParam("token") String token, @QueryParam("yearKey") String yearKey, @QueryParam("classKey") String classKey,
            @QueryParam("branchKey") String branchKey, @QueryParam("periodKey") String periodKey, @QueryParam("keys") List<String> keys,
            @QueryParam("values") List<String> values) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        // TODO 1. Check the token and if this token allows to create a new
        // class.

        Map<String, Object> classInfo = JerseyHelper.listToMap(keys, new ArrayList<Object>(values));

        try {
            BaseObject exam = examService.createExam(requestId, yearKey, classKey, branchKey, periodKey, classInfo);
            RestObject restClass = new RestToKdomConverter().restObjectFromKdom(exam);

            String json = new Gson().toJson(restClass);
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/getExamsInfo")
    @Produces("text/json")
    public String getTeachersInfo(@QueryParam("token") String token, @QueryParam("exams") Set<String> examKeyList,
            @QueryParam("properties") Set<String> properties) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            Set<BaseObject> teachers = new HashSet<>();
            for (String teacherKey : examKeyList) {
                BaseObject exam = examService.getExamProperties(requestId, teacherKey, properties);

                teachers.add(exam);
            }

            List<RestObject> restTeachers = new RestToKdomConverter().restObjectsFromKdoms(teachers);

            String json = new Gson().toJson(restTeachers);
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }
}
