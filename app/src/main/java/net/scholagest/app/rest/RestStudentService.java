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
import net.scholagest.app.utils.JerseyHelper;
import net.scholagest.managers.CoreNamespace;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.IStudentService;

import com.google.gson.Gson;
import com.google.inject.Inject;

@Path("/student")
public class RestStudentService extends AbstractService {
    private final static String REQUEST_ID_PREFIX = "student-";
    private final IStudentService studentService;
    private final IOntologyService ontologyService;
    private JsonConverter converter;

    @Inject
    public RestStudentService(IStudentService studentService, IOntologyService ontologyService) {
        super(ontologyService);
        this.studentService = studentService;
        this.ontologyService = ontologyService;
        this.converter = new JsonConverter(this.ontologyService);
    }

    @GET
    @Path("/create")
    @Produces("text/json")
    public String createStudent(@QueryParam("token") String token, @QueryParam("keys") List<String> keys, @QueryParam("values") List<String> values) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        // TODO 1. Check the token and if this token allows to create a new
        // student.

        Map<String, Object> personalInfo = JerseyHelper.listToMap(keys, new ArrayList<Object>(values));

        // 2. Update the database.
        BaseObject student = null;
        try {
            student = studentService.createStudent(requestId, personalInfo);

            Gson gson = new Gson();
            String json = gson.toJson(converter.convertObjectToJson(student, null));
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/getStudents")
    @Produces("text/json")
    public String getStudents(@QueryParam("token") String token, @QueryParam("properties") Set<String> properties) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            Set<BaseObject> students = studentService.getStudentsWithProperties(requestId, properties);
            List<RestObject> restStudents = new RestToKdomConverter().restObjectsFromKdoms(students);

            String json = new Gson().toJson(restStudents);
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/getStudentsInfo")
    @Produces("text/json")
    public String getStudentsInfo(@QueryParam("token") String token, @QueryParam("students") Set<String> studentKeyList,
            @QueryParam("properties") Set<String> properties) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            Set<BaseObject> students = new HashSet<>();
            for (String studentKey : studentKeyList) {
                BaseObject studentPersonalInfo = studentService.getStudentPersonalProperties(requestId, studentKey, properties);
                BaseObject student = new BaseObject(studentKey, CoreNamespace.tStudent);
                student.setProperties(studentPersonalInfo.getProperties());

                students.add(student);
            }

            List<RestObject> restStudents = new RestToKdomConverter().restObjectsFromKdoms(students);

            String json = new Gson().toJson(restStudents);
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/getProperties")
    @Produces("text/json")
    public String getStudentProperties(@QueryParam("token") String token, @QueryParam("studentKey") String studentKey,
            @QueryParam("properties") Set<String> properties) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            if (properties == null || properties.isEmpty()) {
                properties = ontologyService.getPropertiesForType(CoreNamespace.tStudent);
            }
            BaseObject studentInfo = studentService.getStudentProperties(requestId, studentKey, new HashSet<String>(properties));
            Map<String, OntologyElement> ontology = extractOntology(studentInfo.getProperties().keySet());

            RestObject restStudentInfo = new RestToKdomConverter().restObjectFromKdom(studentInfo);
            new OntologyMerger(ontologyService).mergeOntologyWithRestObject(restStudentInfo, ontology);

            String json = new Gson().toJson(restStudentInfo);
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/getPersonalProperties")
    @Produces("text/json")
    public String getStudentPersonalInfo(@QueryParam("token") String token, @QueryParam("studentKey") String studentKey,
            @QueryParam("properties") Set<String> properties) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            if (properties == null || properties.isEmpty()) {
                properties = ontologyService.getPropertiesForType(CoreNamespace.tStudentPersonalInfo);
            }

            Set<String> personalInfoProperties = ontologyService.filterPropertiesWithCorrectDomain(ScholagestNamespace.tStudentPersonalInfo,
                    new HashSet<String>(properties));

            BaseObject personalObject = studentService.getStudentPersonalProperties(requestId, studentKey, personalInfoProperties);
            Map<String, OntologyElement> personalOntology = extractOntology(personalObject.getProperties().keySet());

            RestObject restPersonalInfo = new RestToKdomConverter().restObjectFromKdom(personalObject);
            new OntologyMerger(ontologyService).mergeOntologyWithRestObject(restPersonalInfo, personalOntology);

            String json = new Gson().toJson(restPersonalInfo);
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @POST
    @Path("/setPersonalProperties")
    @Produces("text/json")
    public String setStudentPersonalInfo(String content) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            RestRequest request = new Gson().fromJson(content, RestRequest.class);
            RestObject requestObject = request.getObject();
            BaseObject baseObject = new RestToKdomConverter().baseObjectFromRest(requestObject);

            studentService.updateStudentProperties(requestId, baseObject.getKey(), baseObject.getProperties(), new HashMap<String, Object>());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }

        return "{}";
    }

    @GET
    @Path("/getMedicalProperties")
    @Produces("text/json")
    public String getStudentMedicalInfo(@QueryParam("token") String token, @QueryParam("studentKey") String studentKey,
            @QueryParam("properties") Set<String> properties) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            if (properties == null || properties.isEmpty()) {
                properties = ontologyService.getPropertiesForType(CoreNamespace.tStudentMedicalInfo);
            }
            Set<String> medicalInfoProperties = ontologyService.filterPropertiesWithCorrectDomain(ScholagestNamespace.tStudentMedicalInfo,
                    new HashSet<String>(properties));

            BaseObject medicalObject = studentService.getStudentMedicalProperties(requestId, studentKey, medicalInfoProperties);
            Map<String, OntologyElement> medicalOntology = extractOntology(medicalObject.getProperties().keySet());

            RestObject restMedicalInfo = new RestToKdomConverter().restObjectFromKdom(medicalObject);
            new OntologyMerger(ontologyService).mergeOntologyWithRestObject(restMedicalInfo, medicalOntology);

            String json = new Gson().toJson(restMedicalInfo);
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @POST
    @Path("/setMedicalProperties")
    @Produces("text/json")
    public String setStudentMedicalInfo(String content) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            RestRequest request = new Gson().fromJson(content, RestRequest.class);
            RestObject requestObject = request.getObject();
            BaseObject baseObject = new RestToKdomConverter().baseObjectFromRest(requestObject);

            studentService.updateStudentProperties(requestId, baseObject.getKey(), new HashMap<String, Object>(), baseObject.getProperties());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }

        return "{}";
    }

    @GET
    @Path("/getStudentsGrades")
    @Produces("text/json")
    public String getStudentsGrades(@QueryParam("token") String token, @QueryParam("studentKeys") Set<String> studentKeys,
            @QueryParam("examKeys") Set<String> examKeys, @QueryParam("yearKey") String yearKey) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {

            Map<String, Map<String, BaseObject>> grades = studentService.getGrades(requestId, studentKeys, examKeys, yearKey);

            String json = gradeMapToJson(grades);
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
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
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            RestStudentGradeList request = new Gson().fromJson(content, RestStudentGradeList.class);
            Map<String, Map<String, RestObject>> restStudentGrades = request.getGrades();

            for (String studentKey : restStudentGrades.keySet()) {
                Map<String, RestObject> restSingleStudentGrades = restStudentGrades.get(studentKey);
                Map<String, BaseObject> studentGrades = convertRestObjectMapToBaseObjectMap(restSingleStudentGrades);

                studentService.setGrades(requestId, studentKey, studentGrades, request.getYearKey(), request.getClassKey(), request.getBranchKey(),
                        request.getPeriodKey());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
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
