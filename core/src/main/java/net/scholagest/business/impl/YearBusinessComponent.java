package net.scholagest.business.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IYearBusinessComponent;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.managers.IYearManager;
import net.scholagest.objects.BaseObject;

import com.google.inject.Inject;

public class YearBusinessComponent implements IYearBusinessComponent {
    private IYearManager yearManager;

    @Inject
    public YearBusinessComponent(IYearManager yearManager) {
        this.yearManager = yearManager;
    }

    @Override
    public BaseObject startYear(String yearName) throws Exception {
        if (yearManager.checkWhetherYearExists(yearName)) {
            throw new ScholagestException(ScholagestExceptionErrorCode.OBJECT_ALREADY_EXISTS, "A year with the same name " + yearName
                    + " already exists");
        }

        BaseObject year = yearManager.createNewYear(yearName);

        yearManager.restoreYear(year.getKey());

        return year;
    }

    @Override
    public void stopYear() throws Exception {
        yearManager.backupYear();
    }

    @Override
    public BaseObject getCurrentYearKey() throws Exception {
        return yearManager.getCurrentYearKey();
    }

    @Override
    public Set<BaseObject> getYearsWithProperties(Set<String> properties) throws Exception {
        Set<BaseObject> years = new HashSet<>();

        for (BaseObject year : yearManager.getYears()) {
            BaseObject yearInfo = yearManager.getYearProperties(year.getKey(), properties);
            years.add(yearInfo);
        }

        return years;
    }

    @Override
    public void setYearProperties(String yearKey, Map<String, Object> yearProperties) {
        yearManager.setYearProperties(yearKey, yearProperties);
    }
}
