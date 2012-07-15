package net.scholagest.services;

import java.util.Map;
import java.util.Set;

public interface IYearService {
    //Start a new year.
    //Throw an exception if a year with the same name already exists or
    //if another year is currently running.
    public String startYear(String name) throws Exception;
    
    //Stop the current year. This implies that all the current scholar
    //information are no more editable. The students' assignments into the
    //classes is backed up and no more link is currently present.
    //Throw an exception if no year with this key is currently running.
    public String stopYear(String yearKey) throws Exception;
    
    public Set<String> getYears() throws Exception;
    
    //Multiple year at once?
    public Set<String> getYearClasses(String yearKey) throws Exception;
    
    public void addClass(String yearKey, String classKey) throws Exception;
    
    public void setInfo(String yearKey,
    		Map<String, Object> properties) throws Exception;
    
    public Map<String, Object> getInfo(String yearKey,
    		Set<String> propertiesName) throws Exception;
}

