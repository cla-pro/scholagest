package net.scholagest.app.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.object.RestRequest;
import net.scholagest.app.rest.object.RestSetPassword;
import net.scholagest.app.utils.HtmlPageBuilder;
import net.scholagest.app.utils.JsonObject;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.IUserService;
import net.scholagest.shiro.RealmAuthenticationAndAuthorization;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.shiro.ShiroException;
import org.apache.shiro.subject.Subject;

import com.google.gson.Gson;
import com.google.inject.Inject;

@Path("/user")
public class RestUserService extends AbstractService {
    private final static String REQUEST_ID_PREFIX = "user-";

    private final IUserService userService;

    @Context
    ServletContext context;

    @Inject
    public RestUserService(final IUserService userService, IOntologyService ontologyService) {
        super(ontologyService);
        this.userService = userService;
    }

    @GET
    @Path("/authenticate")
    @Produces("text/json")
    public String authenticate(@QueryParam("username") String username, @QueryParam("password") String password) {
        try {
            Subject subject = userService.authenticateWithUsername(username, password);

            String token = (String) subject.getPrincipals().fromRealm(RealmAuthenticationAndAuthorization.TOKEN_KEY).iterator().next();

            return new JsonObject("token", token, "nextpage", getBaseUrl() + "services/user/getPage?token=" + token).toString();
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
    @Path("/getPage")
    @Produces(MediaType.TEXT_HTML + ";charset=UTF-8")
    public String getPage(@QueryParam("token") String token) {
        List<String> pages = new ArrayList<>();
        String teacherKey = null;
        try {
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(token));

            pages = userService.getVisibleModules(token);
            teacherKey = userService.getTeacherKeyForToken(token);
        } catch (Exception e) {
            return "<meta http-equiv=\"refresh\" content=\"0; url=" + getBaseUrl() + "html/session_expired.html\" />";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("<div id=\"tabMenu\" data-dojo-type=\"dijit.layout.TabContainer\" " + "style=\"width: 100%; height: 100%;\">");
        builder.append("<script type=\"text/javascript\">" + "var BASE_URL = '" + getBaseUrl() + "'; var myOwnTeacherKey = '" + teacherKey + "';"
                + "dojo.ready(function() { dojo.cookie('scholagest_token', '" + token + "'); " + " });</script>");
        for (String page : pages) {
            String module = loadFile("html" + File.separatorChar + page);

            boolean isJS = page.endsWith(".js");
            if (isJS) {
                builder.append("<script type=\"text/javascript\">");
            }
            builder.append(module);
            if (isJS) {
                builder.append("</script>");
            }
        }
        builder.append("</div>");

        String basePage = loadFile("html" + File.separatorChar + "base.html");
        HtmlPageBuilder htmlPageBuilder = new HtmlPageBuilder("id", "scholagestContent");
        List<String> content = new ArrayList<>();
        content.add(builder.toString());
        String finalPage = htmlPageBuilder.inject(basePage, content);
        return finalPage;
    }

    private String loadFile(String fileName) {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getResourceAsStream(fileName), "UTF-8"));

            String line = reader.readLine();
            while (line != null) {
                builder.append(line + "\n");
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return builder.toString();
    }

    @POST
    @Path("/validateToken")
    @Produces("text/json")
    public String validateToken(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            if (content == null || content.equals("")) {
                return "{errorCode: 0, message: 'Empty content'}";
            }

            RestRequest request = new Gson().fromJson(content, RestRequest.class);

            userService.authenticateWithToken(request.getToken());

            return "{info: {}}";
        } catch (ShiroException e) {
            return handleShiroException(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }
    }

    @POST
    @Path("/setPassword")
    @Produces("text/json")
    public String setPassword(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestSetPassword request = new Gson().fromJson(content, RestSetPassword.class);

            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            userService.setPassword(request.getTeacherKey(), request.getNewPassword());
        } catch (ShiroException e) {
            return handleShiroException(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }

        return "{}";
    }

    @POST
    @Path("/resetPassword")
    @Produces("text/json")
    public String resetPassword(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestSetPassword request = new Gson().fromJson(content, RestSetPassword.class);

            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            userService.resetPassword(request.getTeacherKey());
        } catch (ShiroException e) {
            return handleShiroException(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }

        return "{}";
    }

    @GET
    @Path("/logout")
    @Produces("text/json")
    public String logout(@QueryParam("token") String token) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(token));

            userService.logout(token);
        } catch (ShiroException e) {
            return handleShiroException(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }

        return "{}";
    }
}
