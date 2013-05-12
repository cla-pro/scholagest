package net.scholagest.business.impl;

import java.util.HashSet;
import java.util.Set;

import net.scholagest.business.IYearBusinessComponent;
import net.scholagest.database.ITransaction;
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
    public BaseObject startYear(String requestId, ITransaction transaction, String yearName) throws Exception {
        BaseObject year = yearManager.createNewYear(requestId, transaction, yearName);
        yearManager.restoreYear(requestId, transaction, year.getKey());

        return year;
    }

    @Override
    public void stopYear(String requestId, ITransaction transaction) throws Exception {
        yearManager.backupYear(requestId, transaction);
    }

    @Override
    public BaseObject getCurrentYearKey(String requestId, ITransaction transaction) throws Exception {
        return yearManager.getCurrentYearKey(requestId, transaction);
    }

    @Override
    public Set<BaseObject> getYearsWithProperties(String requestId, ITransaction transaction, Set<String> properties) throws Exception {
        Set<BaseObject> years = new HashSet<>();

        for (BaseObject year : yearManager.getYears(requestId, transaction)) {
            BaseObject yearInfo = yearManager.getYearProperties(requestId, transaction, year.getKey(), properties);
            years.add(yearInfo);
        }

        return years;
    }
}
