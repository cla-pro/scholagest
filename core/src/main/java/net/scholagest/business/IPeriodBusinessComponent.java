package net.scholagest.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.PeriodObject;

public interface IPeriodBusinessComponent {
    public void setPeriodProperties(String periodKey, Map<String, Object> periodProperties) throws Exception;

    public PeriodObject getPeriodProperties(String periodKey, Set<String> properties) throws Exception;
}
