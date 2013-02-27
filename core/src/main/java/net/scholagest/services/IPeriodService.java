package net.scholagest.services;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BaseObject;

public interface IPeriodService {
    public void setPeriodProperties(String requestId, String periodKey, Map<String, Object> periodProperties) throws Exception;

    public BaseObject getPeriodProperties(String requestId, String periodKey, Set<String> properties) throws Exception;
}
