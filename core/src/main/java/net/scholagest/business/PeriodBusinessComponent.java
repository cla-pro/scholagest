package net.scholagest.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.IPeriodManager;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.kdom.DBToKdomConverter;

import com.google.inject.Inject;

public class PeriodBusinessComponent implements IPeriodBusinessComponent {
    private IPeriodManager periodManager;

    @Inject
    public PeriodBusinessComponent(IPeriodManager periodManager) {
        this.periodManager = periodManager;
    }

    @Override
    public void setPeriodProperties(String requestId, ITransaction transaction, String periodKey, Map<String, Object> periodProperties)
            throws Exception {
        periodManager.setPeriodProperties(requestId, transaction, periodKey, periodProperties);
    }

    @Override
    public BaseObject getPeriodProperties(String requestId, ITransaction transaction, String periodKey, Set<String> properties) throws Exception {
        return new DBToKdomConverter().convertDbToKdom(periodManager.getPeriodProperties(requestId, transaction, periodKey, properties));
    }

}
