package net.scholagest.app.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import net.scholagest.app.rest.object.RestGetObjectListRequest;
import net.scholagest.app.rest.object.RestGetObjectRequest;
import net.scholagest.app.rest.object.RestGetObjectSubListRequest;
import net.scholagest.app.rest.object.RestGetStudentGradesRequest;
import net.scholagest.app.rest.object.RestObject;
import net.scholagest.app.rest.object.RestSetObjectRequest;
import net.scholagest.app.rest.object.RestStudentGradeList;
import net.scholagest.app.rest.object.create.RestCreateStudentRequest;
import net.scholagest.app.utils.JerseyHelper;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.namespace.AuthorizationRolesNamespace;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.IStudentService;
import net.scholagest.services.IUserService;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.shiro.ShiroException;
import org.apache.shiro.subject.Subject;

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

    @POST
    @Path("/create")
    @Produces("text/json")
    public String createStudent(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestCreateStudentRequest request = new Gson().fromJson(content, RestCreateStudentRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Map<String, Object> personalInfo = JerseyHelper.listToMap(request.getKeys(), new ArrayList<Object>(request.getValues()));

            // 2. Update the database.
            BaseObject student = studentService.createStudent(personalInfo);

            Gson gson = new Gson();
            String json = gson.toJson(student);
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
    @Path("/getStudents")
    @Produces("text/json")
    public String getStudents(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestGetObjectListRequest request = new Gson().fromJson(content, RestGetObjectListRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Set<BaseObject> students = studentService.getStudentsWithProperties(request.getProperties());
            List<RestObject> restStudents = new RestToKdomConverter().restObjectsFromKdoms(students);

            String json = new Gson().toJson(restStudents);
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
    @Path("/getStudentsInfo")
    @Produces("text/json")
    public String getStudentsInfo(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestGetObjectSubListRequest request = new Gson().fromJson(content, RestGetObjectSubListRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Set<BaseObject> students = new HashSet<>();
            for (String studentKey : request.getKeys()) {
                BaseObject studentPersonalInfo = studentService.getStudentPersonalProperties(studentKey, request.getProperties());
                BaseObject student = new BaseObject(studentKey, CoreNamespace.tStudent);
                student.setProperties(studentPersonalInfo.getProperties());

                students.add(student);
            }

            List<RestObject> restStudents = new RestToKdomConverter().restObjectsFromKdoms(students);

            String json = new Gson().toJson(restStudents);
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
    @Path("/getProperties")
    @Produces("text/json")
    public String getStudentProperties(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestGetObjectRequest request = new Gson().fromJson(content, RestGetObjectRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Set<String> properties = request.getProperties();
            if (properties == null || properties.isEmpty()) {
                properties = ontologyService.getPropertiesForType(CoreNamespace.tStudent);
            }
            BaseObject studentInfo = studentService.getStudentProperties(request.getKey(), properties);
            Map<String, OntologyElement> ontology = extractOntology(studentInfo.getProperties().keySet());

            RestObject restStudentInfo = new RestToKdomConverter().restObjectFromKdom(studentInfo);
            new OntologyMerger(ontologyService).mergeOntologyWithRestObject(restStudentInfo, ontology);

            Subject subject = ScholagestThreadLocal.getSubject();
            if (subject.hasRole(AuthorizationRolesNamespace.ROLE_ADMIN) || subject.isPermitted(request.getKey())) {
                restStudentInfo.setWritable(true);
            }

            String json = new Gson().toJson(restStudentInfo);
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
    @Path("/getPersonalProperties")
    @Produces("text/json")
    public String getStudentPersonalInfo(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestGetObjectRequest request = new Gson().fromJson(content, RestGetObjectRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Set<String> properties = request.getProperties();
            if (properties == null || properties.isEmpty()) {
                properties = ontologyService.getPropertiesForType(CoreNamespace.tStudentPersonalInfo);
            }

            Set<String> personalInfoProperties = ontologyService.filterPropertiesWithCorrectDomain(ScholagestNamespace.tStudentPersonalInfo,
                    new HashSet<String>(properties));

            BaseObject personalObject = studentService.getStudentPersonalProperties(request.getKey(), personalInfoProperties);
            Map<String, OntologyElement> personalOntology = extractOntology(personalObject.getProperties().keySet());

            RestObject restPersonalInfo = new RestToKdomConverter().restObjectFromKdom(personalObject);
            new OntologyMerger(ontologyService).mergeOntologyWithRestObject(restPersonalInfo, personalOntology);

            String json = new Gson().toJson(restPersonalInfo);
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
    @Path("/setPersonalProperties")
    @Produces("text/json")
    public String setStudentPersonalInfo(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestSetObjectRequest request = new Gson().fromJson(content, RestSetObjectRequest.class);

            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            studentService.updateStudentProperties(request.getKey(), request.getProperties(), new HashMap<String, Object>());
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

        return "{}";
    }

    @POST
    @Path("/getMedicalProperties")
    @Produces("text/json")
    public String getStudentMedicalInfo(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestGetObjectRequest request = new Gson().fromJson(content, RestGetObjectRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Set<String> properties = request.getProperties();
            if (properties == null || properties.isEmpty()) {
                properties = ontologyService.getPropertiesForType(CoreNamespace.tStudentMedicalInfo);
            }
            Set<String> medicalInfoProperties = ontologyService.filterPropertiesWithCorrectDomain(ScholagestNamespace.tStudentMedicalInfo,
                    new HashSet<String>(properties));

            BaseObject medicalObject = studentService.getStudentMedicalProperties(request.getKey(), medicalInfoProperties);
            Map<String, OntologyElement> medicalOntology = extractOntology(medicalObject.getProperties().keySet());

            RestObject restMedicalInfo = new RestToKdomConverter().restObjectFromKdom(medicalObject);
            new OntologyMerger(ontologyService).mergeOntologyWithRestObject(restMedicalInfo, medicalOntology);

            String json = new Gson().toJson(restMedicalInfo);
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
    @Path("/setMedicalProperties")
    @Produces("text/json")
    public String setStudentMedicalInfo(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestSetObjectRequest request = new Gson().fromJson(content, RestSetObjectRequest.class);

            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            studentService.updateStudentProperties(request.getKey(), new HashMap<String, Object>(), request.getProperties());
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

        return "{}";
    }

    @POST
    @Path("/getStudentsGrades")
    @Produces("text/json")
    public String getStudentsGrades(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestGetStudentGradesRequest request = new Gson().fromJson(content, RestGetStudentGradesRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Map<String, Map<String, BaseObject>> grades = studentService.getGrades(request.getStudentKeys(), request.getExamKeys(),
                    request.getYearKey());

            String json = gradeMapToJson(grades);
            return "{info: " + json + "}";
        } catch (ShiroException e) {
            return handleShiroException(e);
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
            RestStudentGradeList gradeList = new Gson().fromJson(content, RestStudentGradeList.class);

            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(gradeList.getToken()));

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
            return handleShiroException(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (ScholagestRuntimeException e) {
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
