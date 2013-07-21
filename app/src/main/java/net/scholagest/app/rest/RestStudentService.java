package net.scholagest.app.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.scholagest.app.rest.object.RestObject;
import net.scholagest.app.rest.object.RestRequest;
import net.scholagest.app.rest.object.RestStudentGradeList;
import net.scholagest.app.rest.object.RestStudentGradeRequest;
import net.scholagest.app.utils.JerseyHelper;
import net.scholagest.exception.ScholagestException;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.IStudentService;
import net.scholagest.services.IUserService;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.shiro.ShiroException;

import com.google.gson.Gson;
import com.google.inject.Inject;

@Path("/student")
public class RestStudentService extends AbstractService {
    private final static String REQUEST_ID_PREFIX = "student-";
    private final IStudentService studentService;
    private final IOntologyService ontologyService;
    private final IUserService userService;

    @Inject
    public RestStudentService(IStudentService studentService, IOntologyService ontologyService, IUserService userService) {
        super(ontologyService);
        this.studentService = studentService;
        this.ontologyService = ontologyService;
        this.userService = userService;
    }

    @GET
    @Path("/create")
    @Produces("text/json")
    public String createStudent(@QueryParam("token") String token, @QueryParam("keys") List<String> keys, @QueryParam("values") List<String> values) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(token));

            Map<String, Object> personalInfo = JerseyHelper.listToMap(keys, new ArrayList<Object>(values));

            // 2. Update the database.
            BaseObject student = studentService.createStudent(personalInfo);

            Gson gson = new Gson();
            String json = gson.toJson(student);
            return "{info: " + json + "}";
        } catch (ShiroException e) {
            return generateSessionExpiredMessage(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/getStudents")
    @Produces("text/json")
    public String getStudents(@QueryParam("token") String token, @QueryParam("properties") Set<String> properties) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(token));

            Set<BaseObject> students = studentService.getStudentsWithProperties(properties);
            List<RestObject> restStudents = new RestToKdomConverter().restObjectsFromKdoms(students);

            String json = new Gson().toJson(restStudents);
            return "{info: " + json + "}";
        } catch (ShiroException e) {
            return generateSessionExpiredMessage(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/getStudentsInfo")
    @Produces("text/json")
    public String getStudentsInfo(@QueryParam("token") String token, @QueryParam("students") Set<String> studentKeyList,
            @QueryParam("properties") Set<String> properties) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(token));

            Set<BaseObject> students = new HashSet<>();
            for (String studentKey : studentKeyList) {
                BaseObject studentPersonalInfo = studentService.getStudentPersonalProperties(studentKey, properties);
                BaseObject student = new BaseObject(studentKey, CoreNamespace.tStudent);
                student.setProperties(studentPersonalInfo.getProperties());

                students.add(student);
            }

            List<RestObject> restStudents = new RestToKdomConverter().restObjectsFromKdoms(students);

            String json = new Gson().toJson(restStudents);
            return "{info: " + json + "}";
        } catch (ShiroException e) {
            return generateSessionExpiredMessage(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/getProperties")
    @Produces("text/json")
    public String getStudentProperties(@QueryParam("token") String token, @QueryParam("studentKey") String studentKey,
            @QueryParam("properties") Set<String> properties) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(token));

            if (properties == null || properties.isEmpty()) {
                properties = ontologyService.getPropertiesForType(CoreNamespace.tStudent);
            }
            BaseObject studentInfo = studentService.getStudentProperties(studentKey, new HashSet<String>(properties));
            Map<String, OntologyElement> ontology = extractOntology(studentInfo.getProperties().keySet());

            RestObject restStudentInfo = new RestToKdomConverter().restObjectFromKdom(studentInfo);
            new OntologyMerger(ontologyService).mergeOntologyWithRestObject(restStudentInfo, ontology);

            String json = new Gson().toJson(restStudentInfo);
            return "{info: " + json + "}";
        } catch (ShiroException e) {
            return generateSessionExpiredMessage(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/getPersonalProperties")
    @Produces("text/json")
    public String getStudentPersonalInfo(@QueryParam("token") String token, @QueryParam("studentKey") String studentKey,
            @QueryParam("properties") Set<String> properties) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(token));

            if (properties == null || properties.isEmpty()) {
                properties = ontologyService.getPropertiesForType(CoreNamespace.tStudentPersonalInfo);
            }

            Set<String> personalInfoProperties = ontologyService.filterPropertiesWithCorrectDomain(ScholagestNamespace.tStudentPersonalInfo,
                    new HashSet<String>(properties));

            BaseObject personalObject = studentService.getStudentPersonalProperties(studentKey, personalInfoProperties);
            Map<String, OntologyElement> personalOntology = extractOntology(personalObject.getProperties().keySet());

            RestObject restPersonalInfo = new RestToKdomConverter().restObjectFromKdom(personalObject);
            new OntologyMerger(ontologyService).mergeOntologyWithRestObject(restPersonalInfo, personalOntology);

            String json = new Gson().toJson(restPersonalInfo);
            return "{info: " + json + "}";
        } catch (ShiroException e) {
            return generateSessionExpiredMessage(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }
    }

    @POST
    @Path("/setPersonalProperties")
    @Produces("text/json")
    public String setStudentPersonalInfo(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestRequest request = new Gson().fromJson(content, RestRequest.class);

            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            RestObject requestObject = request.getObject();
            BaseObject baseObject = new RestToKdomConverter().baseObjectFromRest(requestObject);

            studentService.updateStudentProperties(baseObject.getKey(), baseObject.getProperties(), new HashMap<String, Object>());
        } catch (ShiroException e) {
            return generateSessionExpiredMessage(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }

        return "{}";
    }

    @GET
    @Path("/getMedicalProperties")
    @Produces("text/json")
    public String getStudentMedicalInfo(@QueryParam("token") String token, @QueryParam("studentKey") String studentKey,
            @QueryParam("properties") Set<String> properties) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(token));

            if (properties == null || properties.isEmpty()) {
                properties = ontologyService.getPropertiesForType(CoreNamespace.tStudentMedicalInfo);
            }
            Set<String> medicalInfoProperties = ontologyService.filterPropertiesWithCorrectDomain(ScholagestNamespace.tStudentMedicalInfo,
                    new HashSet<String>(properties));

            BaseObject medicalObject = studentService.getStudentMedicalProperties(studentKey, medicalInfoProperties);
            Map<String, OntologyElement> medicalOntology = extractOntology(medicalObject.getProperties().keySet());

            RestObject restMedicalInfo = new RestToKdomConverter().restObjectFromKdom(medicalObject);
            new OntologyMerger(ontologyService).mergeOntologyWithRestObject(restMedicalInfo, medicalOntology);

            String json = new Gson().toJson(restMedicalInfo);
            return "{info: " + json + "}";
        } catch (ShiroException e) {
            return generateSessionExpiredMessage(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }
    }

    @POST
    @Path("/setMedicalProperties")
    @Produces("text/json")
    public String setStudentMedicalInfo(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestRequest request = new Gson().fromJson(content, RestRequest.class);

            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            RestObject requestObject = request.getObject();
            BaseObject baseObject = new RestToKdomConverter().baseObjectFromRest(requestObject);

            studentService.updateStudentProperties(baseObject.getKey(), new HashMap<String, Object>(), baseObject.getProperties());
        } catch (ShiroException e) {
            return generateSessionExpiredMessage(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }

        return "{}";
    }

    @GET
    @Path("/getStudentsGrades")
    @Produces("text/json")
    public String getStudentsGrades(@QueryParam("token") String token, @QueryParam("studentKeys") Set<String> studentKeys,
            @QueryParam("examKeys") Set<String> examKeys, @QueryParam("yearKey") String yearKey) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(token));

            Map<String, Map<String, BaseObject>> grades = studentService.getGrades(studentKeys, examKeys, yearKey);

            String json = gradeMapToJson(grades);
            return "{info: " + json + "}";
        } catch (ShiroException e) {
            return generateSessionExpiredMessage(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }
    }

    private String gradeMapToJson(Map<String, Map<String, BaseObject>> grades) {
        Map<String, Map<String, RestObject>> restGrades = new HashMap<>();
        RestToKdomConverter converter = new RestToKdomConverter();

        for (String studentKey : grades.keySet()) {
            Map<String, BaseObject> studentGrades = grades.get(studentKey);
            Map<String, RestObject> studentRestGrades = new HashMap<>();
            for (String gradeKey : studentGrades.keySet()) {
                BaseObject baseObject = studentGrades.get(gradeKey);
                if (baseObject == null) {
                    studentRestGrades.put(gradeKey, null);
                } else {
                    studentRestGrades.put(gradeKey, converter.restObjectFromKdom(baseObject));
                }
            }

            restGrades.put(studentKey, studentRestGrades);
        }

        return new Gson().toJson(restGrades);
    }

    @POST
    @Path("/setGrades")
    @Produces("text/json")
    public String setStudentsGrades(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestStudentGradeRequest request = new Gson().fromJson(content, RestStudentGradeRequest.class);
            RestStudentGradeList gradeList = request.getGrades();

            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Map<String, Map<String, RestObject>> restStudentGrades = gradeList.getGrades();
            Map<String, RestObject> restPeriodStudentMeans = gradeList.getPeriodMeans();
            Map<String, RestObject> restStudentBranchMeans = gradeList.getBranchMeans();

            for (String studentKey : restStudentGrades.keySet()) {
                Map<String, RestObject> restSingleStudentGrades = restStudentGrades.get(studentKey);
                Map<String, BaseObject> studentGrades = convertRestObjectMapToBaseObjectMap(restSingleStudentGrades);

                studentService.setGrades(studentKey, studentGrades, gradeList.getYearKey(), gradeList.getClassKey(), gradeList.getBranchKey(),
                        gradeList.getPeriodKey());

                RestObject restSingleStudentPeriodMean = restPeriodStudentMeans.get(studentKey);
                BaseObject studentPeriodMean = new RestToKdomConverter().baseObjectFromRest(restSingleStudentPeriodMean);

                studentService.setStudentPeriodMean(studentKey, studentPeriodMean, gradeList.getYearKey(), gradeList.getClassKey(),
                        gradeList.getBranchKey(), gradeList.getPeriodKey());

                RestObject restSingleStudentBranchMean = restStudentBranchMeans.get(studentKey);
                BaseObject studentBranchMean = new RestToKdomConverter().baseObjectFromRest(restSingleStudentBranchMean);

                studentService.setStudentBranchMean(studentKey, studentBranchMean, gradeList.getYearKey(), gradeList.getClassKey(),
                        gradeList.getBranchKey());
            }
        } catch (ShiroException e) {
            return generateSessionExpiredMessage(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:1000, message:'" + e.getMessage() + "'}";
        }

        return "{}";
    }

    private Map<String, BaseObject> convertRestObjectMapToBaseObjectMap(Map<String, RestObject> restObjectMap) {
        Map<String, BaseObject> baseObjectMap = new HashMap<>();
        RestToKdomConverter restToKdomConverter = new RestToKdomConverter();

        for (String key : restObjectMap.keySet()) {
            RestObject restObject = restObjectMap.get(key);
            baseObjectMap.put(key, restToKdomConverter.baseObjectFromRest(restObject));
        }

        return baseObjectMap;
    }
}
