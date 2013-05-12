package net.scholagest.services.impl;

import java.util.HashSet;
import java.util.UUID;

import net.scholagest.business.IYearBusinessComponent;
import net.scholagest.database.ITransaction;
import net.scholagest.services.IYearService;
import net.scholagest.utils.AbstractTest;
import net.scholagest.utils.InMemoryDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class YearServiceTest extends AbstractTest {
    private InMemoryDatabase database;

    private String requestId = UUID.randomUUID().toString();

    private IYearBusinessComponent yearBusinessComponent;
    private IYearService testee;

    @Before
    public void setUpTest() {
        database = Mockito.spy(new InMemoryDatabase());

        yearBusinessComponent = Mockito.mock(IYearBusinessComponent.class);

        testee = new YearService(database, yearBusinessComponent);
    }

    @Test
    public void testStartYear() throws Exception {
        String yearName = "2012-2013";
        testee.startYear(requestId, yearName);

        Mockito.verify(yearBusinessComponent).startYear(Mockito.eq(requestId), (ITransaction) Mockito.any(), Mockito.eq(yearName));
        Mockito.verify(database).getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
    }

    @Test
    public void testStopYear() throws Exception {
        testee.stopYear(requestId);

        Mockito.verify(yearBusinessComponent).stopYear(Mockito.eq(requestId), (ITransaction) Mockito.any());
        Mockito.verify(database).getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
    }

    @Test
    public void testGetCurrentYearKey() throws Exception {
        testee.getCurrentYearKey(requestId);

        Mockito.verify(yearBusinessComponent).getCurrentYearKey(Mockito.eq(requestId), (ITransaction) Mockito.any());
        Mockito.verify(database).getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
    }

    @Test
    public void testGetYearsWithProperties() throws Exception {
        testee.getYearsWithProperties(requestId, new HashSet<String>());

        Mockito.verify(yearBusinessComponent).getYearsWithProperties(Mockito.eq(requestId), (ITransaction) Mockito.any(),
                Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
    }
}
