package net.scholagest.business;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.IYearManager;

import com.google.inject.Inject;

public class YearBusinessComponent implements IYearBusinessComponent {
    private IYearManager yearManager;

    @Inject
    public YearBusinessComponent(IYearManager yearManager) {
        this.yearManager = yearManager;
    }

    @Override
    public String startYear(String requestId, ITransaction transaction, String yearName) throws Exception {
        String yearKey = yearManager.createNewYear(requestId, transaction, yearName);
        yearManager.restoreYear(requestId, transaction, yearKey);

        return yearKey;
    }

    @Override
    public void stopYear(String requestId, ITransaction transaction) throws Exception {
        yearManager.backupYear(requestId, transaction);
    }

    @Override
    public String getCurrentYearKey(String requestId, ITransaction transaction) throws Exception {
        return yearManager.getCurrentYearKey(requestId, transaction);
    }

    @Override
    public Map<String, Map<String, Object>> getYearsWithProperties(String requestId, ITransaction transaction, Set<String> properties)
            throws Exception {
        Map<String, Map<String, Object>> years = new HashMap<String, Map<String, Object>>();

        for (String yearKey : yearManager.getYears(requestId, transaction)) {
            Map<String, Object> info = yearManager.getYearProperties(requestId, transaction, yearKey, properties);
            years.put(yearKey, info);
        }

        return years;
    }
}
