package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;

public interface IObjectManager {
	public String createObject(String requestId, ITransaction transaction,
			String objectKey, String type) throws Exception;
	
	public Map<String, Object> getObjectProperties(String requestId,
			ITransaction transaction, String objectKey,
			Set<String> propertiesName) throws Exception;
	
	public void setObjectProperties(String requestId,
			ITransaction transaction, String objectKey,
			Map<String, Object> properties) throws Exception;
}
