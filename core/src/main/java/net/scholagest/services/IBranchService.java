package net.scholagest.services;

import java.util.Map;
import java.util.Set;

public interface IBranchService {
	public String createBranch(String branchName,
			double coeff) throws Exception;
	
	public void addExam(String branchKey, String examKey) throws Exception;
	
	public Set<String> getBranchExams(String branchKey) throws Exception;
	
	public void setBranchInfo(String branchKey,
			Map<String, Object> properties) throws Exception;
	
	public Map<String, Object> getBranchInfo(String branchKey,
			Set<String> propertiesName) throws Exception;
}
