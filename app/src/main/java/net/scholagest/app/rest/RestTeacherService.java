package net.scholagest.app.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import net.scholagest.app.rest.object.RestGetClassTeacherRequest;
import net.scholagest.app.rest.object.RestGetObjectListRequest;
import net.scholagest.app.rest.object.RestGetObjectRequest;
import net.scholagest.app.rest.object.RestGetObjectSubListRequest;
import net.scholagest.app.rest.object.RestObject;
import net.scholagest.app.rest.object.RestSetObjectRequest;
import net.scholagest.app.rest.object.create.RestCreateTeacherRequest;
import net.scholagest.app.utils.JerseyHelper;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.namespace.AuthorizationRolesNamespace;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.ITeacherService;
import net.scholagest.services.IUserService;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.shiro.ShiroException;
import org.apache.shiro.subject.Subject;

import com.google.gson.Gson;
import com.google.inject.Inject;

@Path("/teacher")
public class RestTeacherService extends AbstractService {
    private final static String REQUEST_ID_PREFIX = "teacher-";
    private final ITeacherService teacherService;
    private final IOntologyService ontologyService;
    private final IUserService userService;

    @Inject
    public RestTeacherService(ITeacherService teacherService, IOntologyService ontologyService, IUserService userService) {
        super(ontologyService);
        this.teacherService = teacherService;
        this.ontologyService = ontologyService;
        this.userService = userService;
    }

    @POST
    @Path("/create")
    @Produces("text/json")
    public String createTeacher(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestCreateTeacherRequest request = new Gson().fromJson(content, RestCreateTeacherRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Map<String, Object> teacherProperties = JerseyHelper.listToMap(request.getKeys(), new ArrayList<Object>(request.getValues()));

            BaseObject teacher = teacherService.createTeacher(request.getTeacherType(), teacherProperties);
            RestObject restTeacher = new RestToKdomConverter().restObjectFromKdom(teacher);

            String json = new Gson().toJson(restTeacher);
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
    @Path("/getTeachers")
    @Produces("text/json")
    public String getTeachers(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestGetObjectListRequest request = new Gson().fromJson(content, RestGetObjectListRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Set<BaseObject> teachers = teacherService.getTeachersWithProperties(request.getProperties());
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

    @POST
    @Path("/getTeachersInfo")
    @Produces("text/json")
    public String getTeachersInfo(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestGetObjectSubListRequest request = new Gson().fromJson(content, RestGetObjectSubListRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Set<BaseObject> teachers = new HashSet<>();
            for (String teacherKey : request.getKeys()) {
                BaseObject teacher = teacherService.getTeacherProperties(teacherKey, request.getProperties());

                teachers.add(teacher);
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

    @POST
    @Path("/getProperties")
    @Produces("text/json")
    public String getTeacherProperties(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestGetObjectRequest request = new Gson().fromJson(content, RestGetObjectRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Set<String> properties = request.getProperties();
            if (properties == null || properties.isEmpty()) {
                properties = ontologyService.getPropertiesForType(CoreNamespace.tTeacher);
            }
            BaseObject teacherInfo = teacherService.getTeacherProperties(request.getKey(), properties);
            Map<String, OntologyElement> ontology = extractOntology(teacherInfo.getProperties().keySet());

            RestObject restTeacherInfo = new RestToKdomConverter().restObjectFromKdom(teacherInfo);
            new OntologyMerger(ontologyService).mergeOntologyWithRestObject(restTeacherInfo, ontology);

            Subject subject = ScholagestThreadLocal.getSubject();
            if (subject.hasRole(AuthorizationRolesNamespace.ROLE_ADMIN) || subject.isPermitted(restTeacherInfo.getKey())) {
                restTeacherInfo.setWritable(true);
            }

            String json = new Gson().toJson(restTeacherInfo);
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
    @Path("/setProperties")
    @Produces("text/json")
    public String setTeacherProperties(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestSetObjectRequest request = new Gson().fromJson(content, RestSetObjectRequest.class);

            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            teacherService.setTeacherProperties(request.getKey(), request.getProperties());
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
    @Path("/getClass")
    @Produces("text/json")
    public String getClass(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestGetClassTeacherRequest request = new Gson().fromJson(content, RestGetClassTeacherRequest.class);

            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            BaseObject teacherInfo = teacherService.getTeacherProperties(request.getTeacherKey(),
                    new HashSet<String>(Arrays.asList(CoreNamespace.pTeacherClasses)));
            Map<String, OntologyElement> ontology = extractOntology(teacherInfo.getProperties().keySet());

            RestObject restTeacherInfo = new RestToKdomConverter().restObjectFromKdom(teacherInfo);
            new OntologyMerger(ontologyService).mergeOntologyWithRestObject(restTeacherInfo, ontology);

            String json = new Gson().toJson(restTeacherInfo);
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
