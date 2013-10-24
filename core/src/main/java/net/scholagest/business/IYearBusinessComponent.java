package net.scholagest.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BaseObject;

public interface IYearBusinessComponent {
    public BaseObject startYear(String yearName) throws Exception;

    public void stopYear() throws Exception;

    public BaseObject getCurrentYearKey() throws Exception;

    public Set<BaseObject> getYearsWithProperties(Set<String> properties) throws Exception;

    public void setYearProperties(String yearKey, Map<String, Object> yearProperties);
}
