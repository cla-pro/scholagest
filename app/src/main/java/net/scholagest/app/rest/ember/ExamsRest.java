package net.scholagest.app.rest.ember;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.AbstractService;
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
        exams.put("3", new Exam("3", "Récitation 3", 3, "4"));
        exams.put("4", new Exam("4", "Récitation 4", 2, "4"));
        exams.put("5", new Exam("5", "Récitation 5", 1, "4"));

        studentResult.put("1", new StudentResult("1", "1", "1", Arrays.asList("1", "2")));
    }

    @Inject
    public ExamsRest(IOntologyService ontologyService) {
        super(ontologyService);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, List<Object>> getExams() {
        Map<String, List<Object>> examsToReturn = new HashMap<>();

        // final List<Object> examsList = new ArrayList<Object>();
        // final List<Object> resultsList = new ArrayList<Object>();
        // for (Exam exam : exams.values()) {
        // examsList.add(exam);
        //
        // for (String resultId : exam.getResults()) {
        // resultsList.add(results.get(resultId));
        // }
        // }
        //
        // examsToReturn.put("exams", examsList);
        // examsToReturn.put("results", resultsList);

        return examsToReturn;
    }

}
