package net.scholagest.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashSet;

import net.scholagest.business.IYearBusinessComponent;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.services.IYearService;
import net.scholagest.utils.AbstractTest;
import net.scholagest.utils.InMemoryDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class YearServiceTest extends AbstractTest {
    private InMemoryDatabase database;

    private IYearBusinessComponent yearBusinessComponent;
    private IYearService testee;

    @Before
    public void setUpTest() {
        database = Mockito.spy(new InMemoryDatabase());
        defineAdminSubject();

        yearBusinessComponent = Mockito.mock(IYearBusinessComponent.class);

        testee = new YearService(database, yearBusinessComponent);
    }

    @Test
    public void testStartYear() throws Exception {
        String yearName = "2012-2013";
        testee.startYear(yearName);

        Mockito.verify(yearBusinessComponent).startYear(Mockito.eq(yearName));
        Mockito.verify(database).getTransaction(Mockito.anyString());
    }

    @Test
    public void testStartYearInsufficientPrivileges() throws Exception {
        String yearName = "2012-2013";

        try {
            defineClassTeacherSubject();
            testee.startYear(yearName);
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineClassHelpTeacherSubject();
            testee.startYear(yearName);
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineOtherTeacherSubject();
            testee.startYear(yearName);
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }
    }

    @Test
    public void testStopYear() throws Exception {
        testee.stopYear();

        Mockito.verify(yearBusinessComponent).stopYear();
        Mockito.verify(database).getTransaction(Mockito.anyString());
    }

    @Test
    public void testStopYearInsufficientPrivileges() throws Exception {
        try {
            defineClassTeacherSubject();
            testee.stopYear();
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineClassHelpTeacherSubject();
            testee.stopYear();
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }

        try {
            defineOtherTeacherSubject();
            testee.stopYear();
            fail("ScholagestException expected");
        } catch (ScholagestException e) {
            assertEquals(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, e.getErrorCode());
        }
    }

    @Test
    public void testGetCurrentYearKey() throws Exception {
        testee.getCurrentYearKey();

        Mockito.verify(yearBusinessComponent).getCurrentYearKey();
        Mockito.verify(database).getTransaction(Mockito.anyString());
    }

    @Test
    public void testGetCurrentYearKeyOtherUsers() throws Exception {
        defineClassTeacherSubject();
        testee.getCurrentYearKey();
        Mockito.verify(yearBusinessComponent).getCurrentYearKey();
        Mockito.reset(yearBusinessComponent);

        defineClassHelpTeacherSubject();
        testee.getCurrentYearKey();
        Mockito.verify(yearBusinessComponent).getCurrentYearKey();
        Mockito.reset(yearBusinessComponent);

        defineOtherTeacherSubject();
        testee.getCurrentYearKey();
        Mockito.verify(yearBusinessComponent).getCurrentYearKey();
    }

    @Test
    public void testGetYearsWithProperties() throws Exception {
        testee.getYearsWithProperties(new HashSet<String>());

        Mockito.verify(yearBusinessComponent).getYearsWithProperties(Mockito.anySetOf(String.class));
        Mockito.verify(database).getTransaction(Mockito.anyString());
    }

    @Test
    public void testGetYearsWithPropertiesOtherUsers() throws Exception {
        defineClassTeacherSubject();
        testee.getYearsWithProperties(new HashSet<String>());
        Mockito.verify(yearBusinessComponent).getYearsWithProperties(Mockito.anySetOf(String.class));
        Mockito.reset(yearBusinessComponent);

        defineClassHelpTeacherSubject();
        testee.getYearsWithProperties(new HashSet<String>());
        Mockito.verify(yearBusinessComponent).getYearsWithProperties(Mockito.anySetOf(String.class));
        Mockito.reset(yearBusinessComponent);

        defineOtherTeacherSubject();
        testee.getYearsWithProperties(new HashSet<String>());
        Mockito.verify(yearBusinessComponent).getYearsWithProperties(Mockito.anySetOf(String.class));
    }
}
