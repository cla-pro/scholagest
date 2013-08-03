package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.PeriodObject;

public interface IPeriodManager {
    public PeriodObject createPeriod(String periodName, String classKey, String branchName, String className, String yearName);

    public void setPeriodProperties(String periodKey, Map<String, Object> periodProperties);

    public PeriodObject getPeriodProperties(String periodKey, Set<String> properties);
}
