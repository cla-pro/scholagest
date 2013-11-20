package net.scholagest.app.rest;

import java.util.Set;
import java.util.UUID;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import net.scholagest.app.rest.object.RestBaseRequest;
import net.scholagest.app.rest.object.RestGetObjectListRequest;
import net.scholagest.app.rest.object.RestStartYearRequest;
import net.scholagest.app.rest.object.RestYearRenameRequest;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.IUserService;
import net.scholagest.services.IYearService;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.shiro.ShiroException;

import com.google.gson.Gson;
import com.google.inject.Inject;

@Path("/year")
public class RestYearService extends AbstractService {
    private final static String REQUEST_ID_PREFIX = "year-";
    private final IYearService yearService;
    private final IUserService userService;
    private JsonConverter converter;

    @Inject
    public RestYearService(IYearService yearService, IOntologyService ontologyService, IUserService userService) {
        super(ontologyService);
        this.yearService = yearService;
        this.userService = userService;
        this.converter = new JsonConverter(ontologyService);
    }

    @POST
    @Path("/start")
    @Produces("text/json")
    public String startYear(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestStartYearRequest request = new Gson().fromJson(content, RestStartYearRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            BaseObject year = yearService.startYear(request.getYearName());

            Gson gson = new Gson();
            String json = gson.toJson(year);
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
    @Path("/stop")
    @Produces("text/json")
    public String stopYear(String content) { // @QueryParam("token") String
                                             // token) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestBaseRequest request = new Gson().fromJson(content, RestBaseRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            yearService.stopYear();
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
    @Path("/getCurrent")
    @Produces("text/json")
    public String getCurrent(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        BaseObject currentYear = null;
        try {
            RestBaseRequest request = new Gson().fromJson(content, RestBaseRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            currentYear = yearService.getCurrentYearKey();

            Gson gson = new Gson();
            String json = gson.toJson(converter.convertObjectToJson(currentYear, null));
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
    @Path("/getYears")
    @Produces("text/json")
    public String getYears(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());
        try {
            RestGetObjectListRequest request = new Gson().fromJson(content, RestGetObjectListRequest.class);

            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Set<BaseObject> yearsInfo = yearService.getYearsWithProperties(request.getProperties());
            BaseObject currentYearKey = yearService.getCurrentYearKey();

            Gson gson = new Gson();
            String years = gson.toJson(converter.convertObjectToJson(yearsInfo));

            String currentYearJson = "";
            if (currentYearKey != null) {
                currentYearJson = ", currentYear: " + gson.toJson(converter.convertObjectToJson(currentYearKey)) + "";
            }
            return "{info: {years: " + years + currentYearJson + "}}";
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
    @Path("/rename")
    @Produces("text/json")
    public String renameYear(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());
        try {
            RestYearRenameRequest request = new Gson().fromJson(content, RestYearRenameRequest.class);

            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            yearService.renameYear(request.getKey(), request.getNewYearName());

            return "{}";
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
