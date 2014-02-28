package net.scholagest.app.rest.ember;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.AbstractService;
import net.scholagest.app.rest.ember.authorization.CheckAuthorization;
import net.scholagest.app.rest.ember.objects.Exam;
import net.scholagest.app.rest.ember.objects.StudentResult;
import net.scholagest.services.IOntologyService;

import com.google.inject.Inject;

@Path("/exams")
public class ExamsRest extends AbstractService {
    public static Map<String, Exam> exams = new HashMap<>();
    public static Map<String, StudentResult> studentResult = new HashMap<>();

    static {
        exams.put("1", new Exam("1", "Récitation 1", 5, "1"));
        exams.put("2", new Exam("2", "Récitation 2", 4, "1"));
        exams.put("6", new Exam("6", "Moyenne", 1, "1"));
        exams.put("3", new Exam("3", "Récitation 3", 3, "4"));
        exams.put("4", new Exam("4", "Récitation 4", 2, "4"));
        exams.put("5", new Exam("5", "Récitation 5", 1, "4"));
        exams.put("7", new Exam("7", "Moyenne", 1, "4"));

        studentResult.put("1", new StudentResult("1", "1", "1", Arrays.asList("1", "2"), "3"));
        studentResult.put("2", new StudentResult("2", "1", "2", Arrays.asList("4", "5", "6"), "7"));
    }

    @Inject
    public ExamsRest(final IOntologyService ontologyService) {
        super(ontologyService);
    }

    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, List<Object>> getExams() {
        final Map<String, List<Object>> examsToReturn = new HashMap<>();

        return examsToReturn;
    }

    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void saveExam(@PathParam("id") final String id, final Map<String, Exam> payload) {
        final Exam exam = payload.get("exam");
        mergeExam(exams.get(id), exam);
    }

    private void mergeExam(final Exam base, final Exam toMerge) {
        base.setCoeff(toMerge.getCoeff());
        base.setName(toMerge.getName());
    }
}
