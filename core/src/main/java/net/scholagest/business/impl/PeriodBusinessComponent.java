package net.scholagest.business.impl;

import java.util.Map;
import java.util.Set;

import net.scholagest.business.IPeriodBusinessComponent;
import net.scholagest.managers.IPeriodManager;
import net.scholagest.objects.PeriodObject;

import com.google.inject.Inject;

public class PeriodBusinessComponent implements IPeriodBusinessComponent {
    private IPeriodManager periodManager;

    @Inject
    public PeriodBusinessComponent(IPeriodManager periodManager) {
        this.periodManager = periodManager;
    }

    @Override
    public void setPeriodProperties(String periodKey, Map<String, Object> periodProperties) throws Exception {
        periodManager.setPeriodProperties(periodKey, periodProperties);
    }

    @Override
    public PeriodObject getPeriodProperties(String periodKey, Set<String> properties) throws Exception {
        return periodManager.getPeriodProperties(periodKey, properties);
    }

}
