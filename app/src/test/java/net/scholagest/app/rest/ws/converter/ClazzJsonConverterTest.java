package net.scholagest.app.rest.ws.converter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import net.scholagest.app.rest.ws.objects.ClazzJson;
import net.scholagest.object.Clazz;

import org.junit.Test;

/**
 * Test class for {@link ClazzJsonConverter}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class ClazzJsonConverterTest {
    @Test
    public void testConvertToTeacherJsonList() {
        final Clazz clazz1 = new Clazz("clazz1", "name1", "year1", Arrays.asList("period1"), Arrays.asList("teacher1"), Arrays.asList("student1"),
                Arrays.asList("branch1"));
        final Clazz clazz2 = new Clazz("clazz2", "name2", "year2", Arrays.asList("period2"), Arrays.asList("teacher2"), Arrays.asList("student2"),
                Arrays.asList("branch2"));
        final List<Clazz> clazzList = Arrays.asList(clazz1, clazz2);

        final ClazzJsonConverter testee = spy(new ClazzJsonConverter());
        final List<ClazzJson> clazzJsonList = testee.convertToClazzJsonList(clazzList);

        assertEquals(clazzList.size(), clazzJsonList.size());
        for (final Clazz clazz : clazzList) {
            verify(testee).convertToClazzJson(eq(clazz));
        }
    }

    @Test
    public void testConvertToTeacherJson() {
        final Clazz clazz = new Clazz("clazz1", "name1", "year1", Arrays.asList("period1"), Arrays.asList("teacher1"), Arrays.asList("student1"),
                Arrays.asList("branch1"));
        final ClazzJson clazzJson = new ClazzJsonConverter().convertToClazzJson(clazz);

        assertEquals(clazz.getId(), clazzJson.getId());
        assertEquals(clazz.getName(), clazzJson.getName());
        assertEquals(clazz.getYear(), clazzJson.getYear());
        assertEquals(clazz.getPeriods(), clazzJson.getPeriods());
        assertEquals(clazz.getTeachers(), clazzJson.getTeachers());
        assertEquals(clazz.getStudents(), clazzJson.getStudents());
        assertEquals(clazz.getBranches(), clazzJson.getBranches());
    }

    @Test
    public void testConvertToTeacher() {
        final ClazzJson clazzJson = new ClazzJson("clazz1", "name1", "year1", Arrays.asList("period1"), Arrays.asList("teacher1"),
                Arrays.asList("student1"), Arrays.asList("branch1"));
        final Clazz clazz = new ClazzJsonConverter().convertToClazz(clazzJson);

        assertEquals(clazzJson.getId(), clazz.getId());
        assertEquals(clazzJson.getName(), clazz.getName());
        assertEquals(clazzJson.getYear(), clazz.getYear());
        assertEquals(clazzJson.getPeriods(), clazz.getPeriods());
        assertEquals(clazzJson.getTeachers(), clazz.getTeachers());
        assertEquals(clazzJson.getStudents(), clazz.getStudents());
        assertEquals(clazzJson.getBranches(), clazz.getBranches());
    }
}
