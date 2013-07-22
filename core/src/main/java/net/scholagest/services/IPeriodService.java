package net.scholagest.services;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BaseObject;

public interface IPeriodService {
    public void setPeriodProperties(String periodKey, Map<String, Object> periodProperties) throws Exception;

    public BaseObject getPeriodProperties(String periodKey, Set<String> properties) throws Exception;
    
    public Map<String, Map<String, BaseObject>> getPeriodMeans(String periodKey, Set<String> studentKeys) throws Exception;
}
