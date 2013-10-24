package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BaseObject;

public interface IYearManager {
    public BaseObject createNewYear(String yearName);

    public void restoreYear(String yearKey) throws Exception;

    public void backupYear() throws Exception;

    public BaseObject getCurrentYearKey() throws Exception;

    public Set<BaseObject> getYears() throws Exception;

    public BaseObject getYearProperties(String yearKey, Set<String> propertiesName);

    public void addClassToYear(String yearKey, String classKey);

    public boolean checkWhetherYearExists(String yearName);

    public void setYearProperties(String yearKey, Map<String, Object> yearProperties);
}
