package net.scholagest.app.rest.ws.converter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import net.scholagest.app.rest.ws.objects.YearJson;
import net.scholagest.object.Year;

import org.junit.Test;

/**
 * Test class for {@link YearJsonConverter}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class YearJsonConverterTest {
    @Test
    public void testConvertToYearJsonList() {
        final Year year1 = new Year("year1", "name1", false, Arrays.asList("class1", "class2"));
        final Year year2 = new Year("year2", "name2", true, Arrays.asList("class3"));

        final List<Year> toConvert = Arrays.asList(year1, year2);
        final YearJsonConverter testee = spy(new YearJsonConverter());

        final List<YearJson> result = testee.convertToYearJsonList(toConvert);

        assertEquals(toConvert.size(), result.size());

        for (final Year year : toConvert) {
            verify(testee).convertToYearJson(eq(year));
        }
    }

    @Test
    public void testConvertToYearJson() {
        final Year year = new Year("year", "name", false, Arrays.asList("class1", "class2"));

        final YearJsonConverter testee = new YearJsonConverter();
        final YearJson converted = testee.convertToYearJson(year);

        assertEquals(year.getId(), converted.getId());
        assertEquals(year.getName(), converted.getName());
        assertEquals(year.isRunning(), converted.isRunning());
        assertEquals(year.getClasses(), converted.getClasses());
    }

    @Test
    public void testConvertToYear() {
        final YearJson yearJson = new YearJson("year", "name", false, Arrays.asList("class1", "class2"));

        final YearJsonConverter testee = new YearJsonConverter();
        final Year converted = testee.convertToYear(yearJson);

        assertEquals(yearJson.getId(), converted.getId());
        assertEquals(yearJson.getName(), converted.getName());
        assertEquals(yearJson.isRunning(), converted.isRunning());
        assertEquals(yearJson.getClasses(), converted.getClasses());
    }
}
