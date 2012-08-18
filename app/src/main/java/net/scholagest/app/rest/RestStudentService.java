package net.scholagest.app.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.scholagest.app.utils.JerseyHelper;
import net.scholagest.app.utils.JsonObject;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.IStudentService;

import com.google.inject.Inject;

@Path("/student")
public class RestStudentService {
	private final static String REQUEST_ID_PREFIX = "student-";
	private final IStudentService studentService;
	private final IOntologyService ontologyService;
	
	@Inject
	public RestStudentService(IStudentService studentService,
			IOntologyService ontologyService) {
		this.studentService = studentService;
		this.ontologyService = ontologyService;
	}

	@GET
	@Path("/create")
	@Produces("text/json")
	public String createTeacher(
			@QueryParam("token") String token,
			@QueryParam("keys") List<String> keys,
			@QueryParam("values") List<String> values) {
		String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
		//TODO 1. Check the token and if this token allows to create a new
		//student.
		
		Map<String, Object> personalInfo = JerseyHelper.listToMap(keys,
				new ArrayList<Object>(values));

		//2. Update the database.
		String teacherKey = null;
		try {
			teacherKey = this.studentService.createStudent(requestId, personalInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return "{errorCode=0, message='" + e.getMessage() + "'}";
		}
		
		return new JsonObject("studentKey", teacherKey).toString();
	}
}
