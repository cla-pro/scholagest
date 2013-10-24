package net.scholagest.services;

import java.util.Set;

import net.scholagest.objects.BaseObject;

public interface IYearService {
    // Start a new year.
    // Throw an exception if a year with the same name already exists or
    // if another year is currently running.
    public BaseObject startYear(String yearName) throws Exception;

    // Stop the current year. This implies that all the current scholar
    // information are no more editable. The students' assignments into the
    // classes is backed up and no more link is currently present.
    public void stopYear() throws Exception;

    public BaseObject getCurrentYearKey() throws Exception;

    public Set<BaseObject> getYearsWithProperties(Set<String> properties) throws Exception;

    public void renameYear(String yearKey, String newYearName) throws Exception;
}
