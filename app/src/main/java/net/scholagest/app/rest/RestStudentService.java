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
import net.scholagest.app.utils.JsonObject;
import net.scholagest.managers.CoreNamespace;
import net.scholagest.managers.ontology.RDFS;
import net.scholagest.managers.ontology.parser.OntologyElement;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.IStudentService;

import com.google.gson.Gson;
import com.google.inject.Inject;

@Path("/student")
public class RestStudentService {
    private final static String REQUEST_ID_PREFIX = "student-";
    private final IStudentService studentService;
    private final IOntologyService ontologyService;

    @Inject
    public RestStudentService(IStudentService studentService, IOntologyService ontologyService) {
        this.studentService = studentService;
        this.ontologyService = ontologyService;
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
        String teacherKey = null;
        try {
            teacherKey = this.studentService.createStudent(requestId, personalInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }

        return new JsonObject("studentKey", teacherKey).toString();
    }

    @GET
    @Path("/getStudents")
    @Produces("text/json")
    public String getStudents(@QueryParam("token") String token, @QueryParam("properties") Set<String> properties) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            Map<String, Map<String, Object>> teachersInfo = studentService.getStudentsWithProperties(requestId, properties);

            Gson gson = new Gson();
            String json = gson.toJson(teachersInfo);
            return "{students: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/getProperties")
    @Produces("text/json")
    public String getStudentInfo(@QueryParam("token") String token, @QueryParam("studentKey") String studentKey,
            @QueryParam("properties") List<String> properties) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            Set<String> personalInfoProperties = ontologyService.filterPropertiesWithCorrectDomain(ScholagestNamespace.tStudentPersonalInfo,
                    new HashSet<String>(properties));
            Set<String> medicalInfoProperties = ontologyService.filterPropertiesWithCorrectDomain(ScholagestNamespace.tStudentMedicalInfo,
                    new HashSet<String>(properties));

            Map<String, Object> info = new HashMap<String, Object>();
            info.put(ScholagestNamespace.pStudentPersonalInfo,
                    this.studentService.getStudentPersonalInfo(requestId, studentKey, personalInfoProperties));
            info.put(ScholagestNamespace.pStudentMedicalInfo, this.studentService.getStudentMedicalInfo(requestId, studentKey, medicalInfoProperties));

            Map<String, Map<String, Object>> result = extractOntology(info);

            Gson gson = new Gson();
            String json = gson.toJson(result);
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Map<String, Object>> extractOntology(Map<String, Object> info) throws Exception {
        Map<String, Map<String, Object>> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : info.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Map) {
                value = extractOntology((Map<String, Object>) value);
            }
            // Get ontology for the field.
            OntologyElement element = ontologyService.getElementWithName(entry.getKey());
            Map<String, Object> fieldInfo = new HashMap<>();
            fieldInfo.put("value", value);
            String displayText = element.getAttributes().get(CoreNamespace.scholagestNs + "#displayText");
            fieldInfo.put("displayText", displayText);
            boolean isRangeGroup = ontologyService.isSubtypeOf(element.getAttributeWithName(RDFS.range), ScholagestNamespace.tGroup);
            if (isRangeGroup) {
                fieldInfo.put("isHtmlGroup", true);
            }

            result.put(entry.getKey(), fieldInfo);
        }

        return result;
    }
}
