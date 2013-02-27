package net.scholagest.services;

import java.util.Map;
import java.util.Set;

import net.scholagest.business.IPeriodBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.objects.BaseObject;

import com.google.inject.Inject;

public class PeriodService implements IPeriodService {
    private final IDatabase database;
    private final IPeriodBusinessComponent periodBusinessComponent;

    @Inject
    public PeriodService(IDatabase database, IPeriodBusinessComponent periodBusinessComponent) {
        this.database = database;
        this.periodBusinessComponent = periodBusinessComponent;
    }

    @Override
    public void setPeriodProperties(String requestId, String periodKey, Map<String, Object> periodProperties) throws Exception {
        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            periodBusinessComponent.setPeriodProperties(requestId, transaction, periodKey, periodProperties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public BaseObject getPeriodProperties(String requestId, String periodKey, Set<String> properties) throws Exception {
        BaseObject periodInfo = null;

        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            periodInfo = periodBusinessComponent.getPeriodProperties(requestId, transaction, periodKey, properties);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return periodInfo;
    }

}
