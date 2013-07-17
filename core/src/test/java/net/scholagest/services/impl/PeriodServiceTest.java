package net.scholagest.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import net.scholagest.business.IOntologyBusinessComponent;
import net.scholagest.business.IPeriodBusinessComponent;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObjectMock;
import net.scholagest.objects.PeriodObject;
import net.scholagest.services.IPeriodService;
import net.scholagest.utils.AbstractTest;
import net.scholagest.utils.InMemoryDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PeriodServiceTest extends AbstractTest {
    private static final String CLASS_KEY = "classKey";

    private InMemoryDatabase database;
    private IPeriodBusinessComponent periodBusinessComponent;
    private IOntologyBusinessComponent ontologyBusinessComponent;

    private IPeriodService testee;

    @Before
    public void setup() {
        database = Mockito.spy(new InMemoryDatabase());
        defineAdminSubject();

        periodBusinessComponent = Mockito.mock(IPeriodBusinessComponent.class);
        ontologyBusinessComponent = Mockito.mock(IOntologyBusinessComponent.class);

        testee = new PeriodService(database, periodBusinessComponent, ontologyBusinessComponent);
    }

    @Test
    public void testSetPeriodProperties() throws Exception {
        String periodKey = "periodKey";

        Mockito.when(periodBusinessComponent.getPeriodProperties(periodKey, new HashSet<String>(Arrays.asList(CoreNamespace.pPeriodClass))))
                .thenReturn(createClassKeyObject());

        testee.setPeriodProperties(periodKey, new HashMap<String, Object>());

        Mockito.verify(periodBusinessComponent).setPeriodProperties(Mockito.eq(periodKey), Mockito.anyMapOf(String.class, Object.class));
        Mockito.verify(periodBusinessComponent).getPeriodProperties(Mockito.eq(periodKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testSetPeriodPropertiesInsufficientPrivilegies() throws Exception {
        String periodKey = "periodKey";

        Mockito.when(periodBusinessComponent.getPeriodProperties(periodKey, new HashSet<String>(Arrays.asList(CoreNamespace.pPeriodClass))))
                .thenReturn(createClassKeyObject());

        defineClassTeacherSubject();
        testee.setPeriodProperties(periodKey, new HashMap<String, Object>());
        Mockito.verify(periodBusinessComponent).setPeriodProperties(Mockito.eq(periodKey), Mockito.anyMapOf(String.class, Object.class));
        Mockito.verify(periodBusinessComponent).getPeriodProperties(Mockito.eq(periodKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());

        try {
            defineClassHelpTeacherSubject();
            testee.setPeriodProperties(periodKey, new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineOtherTeacherSubject();
            testee.setPeriodProperties(periodKey, new HashMap<String, Object>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }
    }

    @Test
    public void testGetPeriodProperties() throws Exception {
        String periodKey = "periodKey";

        Mockito.when(periodBusinessComponent.getPeriodProperties(periodKey, new HashSet<String>(Arrays.asList(CoreNamespace.pPeriodClass))))
                .thenReturn(createClassKeyObject());

        testee.getPeriodProperties(periodKey, new HashSet<String>());

        Mockito.verify(periodBusinessComponent, Mockito.times(2)).getPeriodProperties(Mockito.eq(periodKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testGetPeriodPropertiesInsufficientPrivilegies() throws Exception {
        String periodKey = "periodKey";

        Mockito.when(periodBusinessComponent.getPeriodProperties(periodKey, new HashSet<String>(Arrays.asList(CoreNamespace.pPeriodClass))))
                .thenReturn(createClassKeyObject());

        defineClassTeacherSubject();
        testee.getPeriodProperties(periodKey, new HashSet<String>());
        Mockito.verify(periodBusinessComponent, Mockito.times(2)).getPeriodProperties(Mockito.eq(periodKey), Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(getKeyspace());

        try {
            defineClassHelpTeacherSubject();
            testee.getPeriodProperties(periodKey, new HashSet<String>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineOtherTeacherSubject();
            testee.getPeriodProperties(periodKey, new HashSet<String>());
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }
    }

    private PeriodObject createClassKeyObject() {
        PeriodObject periodObject = BaseObjectMock.createPeriodObject(null, new HashMap<String, Object>());

        periodObject.putProperty(CoreNamespace.pPeriodClass, CLASS_KEY);

        return periodObject;
    }
}
