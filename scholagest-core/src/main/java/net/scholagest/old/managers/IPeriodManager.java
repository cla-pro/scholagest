package net.scholagest.old.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.old.objects.PeriodObject;

public interface IPeriodManager {
    public PeriodObject createPeriod(String periodName, String classKey);

    public void setPeriodProperties(String periodKey, Map<String, Object> periodProperties);

    public PeriodObject getPeriodProperties(String periodKey, Set<String> properties);
}
