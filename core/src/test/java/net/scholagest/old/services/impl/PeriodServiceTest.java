package net.scholagest.old.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.old.business.IOntologyBusinessComponent;
import net.scholagest.old.business.IPeriodBusinessComponent;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BaseObjectMock;
import net.scholagest.old.objects.PeriodObject;
import net.scholagest.old.services.IPeriodService;
import net.scholagest.old.services.impl.PeriodService;
import net.scholagest.utils.old.AbstractTest;
import net.scholagest.utils.old.InMemoryDatabase;

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

        when(periodBusinessComponent.getPeriodProperties(periodKey, new HashSet<String>(Arrays.asList(CoreNamespace.pPeriodClass)))).thenReturn(
                createClassKeyObject());

        testee.setPeriodProperties(periodKey, new HashMap<String, Object>());

        verify(periodBusinessComponent).setPeriodProperties(eq(periodKey), anyMapOf(String.class, Object.class));
        verify(periodBusinessComponent).getPeriodProperties(eq(periodKey), anySetOf(String.class));
        verify(database).getTransaction(getKeyspace());
    }

    @Test
    public void testSetPeriodPropertiesInsufficientPrivilegies() throws Exception {
        String periodKey = "periodKey";

        Mockito.when(periodBusinessComponent.getPeriodProperties(periodKey, new HashSet<String>(Arrays.asList(CoreNamespace.pPeriodClass))))
                .thenReturn(createClassKeyObject());

        defineClassTeacherSubject();
        testee.setPeriodProperties(periodKey, new HashMap<String, Object>());
        verify(periodBusinessComponent).setPeriodProperties(eq(periodKey), anyMapOf(String.class, Object.class));
        verify(periodBusinessComponent).getPeriodProperties(eq(periodKey), anySetOf(String.class));
        verify(database).getTransaction(getKeyspace());

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

        when(periodBusinessComponent.getPeriodProperties(periodKey, new HashSet<String>(Arrays.asList(CoreNamespace.pPeriodClass)))).thenReturn(
                createClassKeyObject());

        defineClassTeacherSubject();
        testee.getPeriodProperties(periodKey, new HashSet<String>());
        verify(periodBusinessComponent, times(2)).getPeriodProperties(eq(periodKey), anySetOf(String.class));
        verify(database).getTransaction(getKeyspace());

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
        periodObject.flushAllProperties();

        return periodObject;
    }
}
