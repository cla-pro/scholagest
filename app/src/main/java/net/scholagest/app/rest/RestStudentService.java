package net.scholagest.app.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

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
            Set<BaseObject> studentsInfo = studentService.getStudentsWithProperties(requestId, properties);

            Gson gson = new Gson();
            String json = gson.toJson(converter.convertObjectToJson(studentsInfo));
            return "{students: " + json + "}";
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
            Map<String, Object> jsonPersonalObject = converter.convertObjectToJson(personalObject, personalOntology);

            OntologyElement pMedicalOntology = extractOntology(CoreNamespace.pStudentPersonalInfo);

            String json = new Gson().toJson(converter.convertPropertyToJson(pMedicalOntology, jsonPersonalObject));
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/setPersonalProperties")
    @Produces("text/json")
    public String setStudentPersonalInfo(@QueryParam("token") String token, @QueryParam("studentKey") String studentKey,
            @QueryParam("names") List<String> names, @QueryParam("values") List<String> values) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            Map<String, Object> properties = JerseyHelper.listToMap(names, new ArrayList<Object>(values));

            studentService.updateStudentProperties(requestId, studentKey, properties, new HashMap<String, Object>());
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
            Map<String, Object> jsonMedicalObject = converter.convertObjectToJson(medicalObject, medicalOntology);

            OntologyElement pMedicalOntology = extractOntology(CoreNamespace.pStudentMedicalInfo);

            String json = new Gson().toJson(converter.convertPropertyToJson(pMedicalOntology, jsonMedicalObject));
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/setMedicalProperties")
    @Produces("text/json")
    public String setStudentMedicalInfo(@QueryParam("token") String token, @QueryParam("studentKey") String studentKey,
            @QueryParam("names") List<String> names, @QueryParam("values") List<String> values) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            Map<String, Object> properties = JerseyHelper.listToMap(names, new ArrayList<Object>(values));

            studentService.updateStudentProperties(requestId, studentKey, new HashMap<String, Object>(), properties);
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }

        return "{}";
    }
}
