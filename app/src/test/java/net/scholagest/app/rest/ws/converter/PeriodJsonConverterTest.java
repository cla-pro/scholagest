package net.scholagest.app.rest.ws.converter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import net.scholagest.app.rest.ws.objects.PeriodJson;
import net.scholagest.object.Period;

import org.junit.Test;

/**
 * Test class for {@link PeriodJsonConverter}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class PeriodJsonConverterTest {
    @Test
    public void testConvertToTeacherJsonList() {
        final Period period1 = new Period("period1", "name1", "clazz1", Arrays.asList("branchPeriod1"));
        final Period period2 = new Period("period2", "name2", "clazz2", Arrays.asList("branchPeriod2"));
        final List<Period> periodList = Arrays.asList(period1, period2);

        final PeriodJsonConverter testee = spy(new PeriodJsonConverter());
        final List<PeriodJson> periodJsonList = testee.convertToPeriodJsonList(periodList);

        assertEquals(periodList.size(), periodJsonList.size());
        for (final Period period : periodList) {
            verify(testee).convertToPeriodJson(eq(period));
        }
    }

    @Test
    public void testConvertToTeacherJson() {
        final Period period = new Period("period1", "name1", "clazz1", Arrays.asList("branchPeriod1"));
        final PeriodJson periodJson = new PeriodJsonConverter().convertToPeriodJson(period);

        assertEquals(period.getId(), periodJson.getId());
        assertEquals(period.getName(), periodJson.getName());
        assertEquals(period.getClazz(), periodJson.getClazz());
        assertEquals(period.getBranchPeriods(), periodJson.getBranchPeriods());
    }

    @Test
    public void testConvertToTeacher() {
        final PeriodJson periodJson = new PeriodJson("period1", "name1", "clazz1", Arrays.asList("branchPeriod1"));
        final Period period = new PeriodJsonConverter().convertToPeriod(periodJson);

        assertEquals(periodJson.getId(), period.getId());
        assertEquals(periodJson.getName(), period.getName());
        assertEquals(periodJson.getClazz(), period.getClazz());
        assertEquals(periodJson.getBranchPeriods(), period.getBranchPeriods());
    }
}
