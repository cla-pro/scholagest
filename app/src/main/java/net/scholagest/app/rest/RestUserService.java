package net.scholagest.app.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.utils.HtmlPageBuilder;
import net.scholagest.app.utils.JsonObject;
import net.scholagest.database.DatabaseException;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.services.IUserService;

import org.joda.time.DateTime;

import com.google.inject.Inject;

@Path("/user")
public class RestUserService {
    private final IDatabase database;
    private final IUserService userService;

    @Context
    ServletContext context;

    @Inject
    public RestUserService(final IDatabase database, final IUserService userService) {
        this.database = database;
        this.userService = userService;
    }

    @GET
    @Path("/authenticate")
    @Produces("text/json")
    public String authenticate(@QueryParam("username") String username, @QueryParam("password") String password,
            @Context HttpServletResponse servletResponse) {
        // TODO 1. check the username/password

        String token = UUID.randomUUID().toString();

        // 2. store the token and the information into the database.
        ITransaction transaction = this.database.getTransaction(ScholagestNamespace.scholagestKeyspace);
        try {
            transaction.insert(token, ScholagestNamespace.pSessionExpirationDate, new DateTime().plusHours(2).toString("yyyy-MM-dd HH:mm:ss"), null);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        return new JsonObject("token", token, "nextpage", "http://localhost:8080/scholagest-app/services/user/getPage").toString();
    }

    @GET
    @Path("/getPage")
    @Produces(MediaType.TEXT_HTML)
    public String getPage(@QueryParam("token") String token) {
        String[] pages;
        try {
            pages = userService.getVisibleModules(UUID.randomUUID().toString(), token);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        StringBuilder builder = new StringBuilder();

        builder.append("<div id=\"tabMenu\" data-dojo-type=\"dijit.layout.TabContainer\" " + "style=\"width: 100%; height: 100%;\">");
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
        return htmlPageBuilder.inject(basePage, content);
    }

    private String loadFile(String fileName) {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getResourceAsStream(fileName)));

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

    @GET
    @Path("/closeSession")
    @Produces("text/json")
    public String closeSession(@QueryParam("token") String token) {
        ITransaction transaction = this.database.getTransaction(ScholagestNamespace.scholagestKeyspace);
        try {
            for (String column : transaction.getColumns(token)) {
                transaction.delete(token, column, null);
            }

        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        return new JsonObject("newURL", "login.html").toString();
    }
}
