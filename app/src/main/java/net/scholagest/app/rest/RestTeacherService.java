package net.scholagest.app.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.scholagest.app.utils.JerseyHelper;
import net.scholagest.app.utils.JsonObject;
import net.scholagest.managers.ontology.parser.OntologyElement;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.ITeacherService;

import com.google.gson.Gson;
import com.google.inject.Inject;

@Path("/teacher")
public class RestTeacherService {
	private final ITeacherService teacherService;
	private final IOntologyService ontologyService;
	
	@Inject
	public RestTeacherService(ITeacherService teacherService,
			IOntologyService ontologyService) {
		this.teacherService = teacherService;
		this.ontologyService = ontologyService;
	}
	
	@GET
	@Path("/createTeacher")
	@Produces("text/json")
	public String createTeacher(
			@QueryParam("token") String token,
			@QueryParam("teacherType") String teacherType,
			@QueryParam("keys") List<String> keys,
			@QueryParam("values") List<String> values) {
		//TODO 1. Check the token and if this token allows to create a new
		//teacher.
		
		Map<String, Object> teacherProperties = JerseyHelper.listToMap(keys,
				new ArrayList<Object>(values));

		//2. Update the database.
		String teacherKey = null;
		try {
			teacherKey = this.teacherService.createTeacher(teacherType,
					teacherProperties);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new JsonObject("teacherKey", teacherKey).toString();
	}
	
	@GET
	@Path("/getTeachers")
	@Produces("text/json")
	public String getTeachers(
			@QueryParam("token") String token,
			@QueryParam("properties") Set<String> properties) {
		try {
			Map<String, Map<String, Object>> teachersInfo =
					teacherService.getTeachersWithProperties(properties);
			
			Gson gson = new Gson();
			String json = gson.toJson(teachersInfo);
			return "{teachers: " + json + "}";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{}";
	}
	
	@GET
	@Path("/getProperties")
	@Produces("text/json")
	public String getTeacherInfo(
			@QueryParam("token") String token,
			@QueryParam("teacherKey") String teacherKey,
			@QueryParam("properties") List<String> properties) {
		try {
			Map<String, Object> info = this.teacherService.getTeacherInfo(
					teacherKey, new HashSet<String>(properties));
			
			Map<String, Map<String, Object>> result = new HashMap<>();
			for (Map.Entry<String, Object> entry : info.entrySet()) {
				//Get ontology for the field.
				OntologyElement element = this.ontologyService.
						getElementWithName(entry.getKey());
				Map<String, Object> fieldInfo = new HashMap<>();
				fieldInfo.put("value", entry.getValue());
				String displayText = element.getAttributes().get("sg:displayText");
				fieldInfo.put("displayText", displayText);
				
				result.put(entry.getKey(), fieldInfo);
			}
			
			Gson gson = new Gson();
			String json = gson.toJson(result);
			return "{info: " + json + "}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{errorCode=0, message='" + e.getMessage() + "'}";
		}
	}
	
	@GET
	@Path("/setProperties")
	@Produces("text/json")
	public String setTeacherInfo(
			@QueryParam("token") String token,
			@QueryParam("teacherKey") String teacherKey,
			@QueryParam("names") List<String> names,
			@QueryParam("values") List<String> values) {
		try {
			Map<String, Object> properties = JerseyHelper.listToMap(names,
					new ArrayList<Object>(values));
			
			this.teacherService.setTeacherInfo(teacherKey, properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "{}";
	}
}
