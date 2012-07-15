package net.scholagest.services;

import java.util.Map;
import java.util.Set;

public interface IExamService {
	public String createExam(String examName) throws Exception;
	
	public void addGrade(String examKey, String studentKey,
			String value) throws Exception;
	
	public void setExamInfo(String examKey, Map<String, Object> properties)
			throws Exception;
	
	public Map<String, Object> getExamInfo(String examKey,
			Set<String> propertiesName) throws Exception;
}
