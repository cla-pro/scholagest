package net.scholagest.old.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.objects.PeriodObject;

public interface IPeriodBusinessComponent {
    public void setPeriodProperties(String periodKey, Map<String, Object> periodProperties) throws Exception;

    public PeriodObject getPeriodProperties(String periodKey, Set<String> properties) throws Exception;
    
    public Map<String, Map<String, BaseObject>> getPeriodMeans(String periodKey, Set<String> studentKeys);
}
