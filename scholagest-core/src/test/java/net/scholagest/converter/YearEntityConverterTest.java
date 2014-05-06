package net.scholagest.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import net.scholagest.ReflectionUtils;
import net.scholagest.db.entity.ClazzEntity;
import net.scholagest.db.entity.YearEntity;
import net.scholagest.object.Year;

import org.junit.Test;

/**
 * Test class for {@link YearEntityConverter}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class YearEntityConverterTest {
    @Test
    public void testConvertToYearList() {
        final YearEntity yearEntity1 = createYearEntity(1L, "name1", true);
        final YearEntity yearEntity2 = createYearEntity(2L, "name2", true);
        final List<YearEntity> toConvert = Arrays.asList(yearEntity1, yearEntity2);

        final YearEntityConverter testee = spy(new YearEntityConverter());
        final List<Year> converted = testee.convertToYearList(toConvert);

        assertEquals(toConvert.size(), converted.size());
        for (final YearEntity yearEntity : toConvert) {
            verify(testee).convertToYear(eq(yearEntity));
        }
    }

    @Test
    public void testConvertToYear() {
        final YearEntity yearEntity = createYearEntity(1L, "name", true);

        final YearEntityConverter testee = spy(new YearEntityConverter());
        final Year converted = testee.convertToYear(yearEntity);

        assertEquals(yearEntity.getId().toString(), converted.getId());
        assertEquals(yearEntity.getName(), converted.getName());
        assertEquals(yearEntity.isRunning(), converted.isRunning());

        assertEquals(yearEntity.getClasses().size(), converted.getClasses().size());
        for (final ClazzEntity clazzEntity : yearEntity.getClasses()) {
            assertTrue(converted.getClasses().contains("" + clazzEntity.getId()));
        }
    }

    @Test
    public void testConvertToYearEntity() {
        final Year year = new Year("4", "name", true, Arrays.asList("1", "2"));

        final YearEntityConverter testee = spy(new YearEntityConverter());
        final YearEntity converted = testee.convertToYearEntity(year);

        assertNull(converted.getId());
        assertEquals(year.getName(), converted.getName());
        assertEquals(year.isRunning(), converted.isRunning());
        assertTrue(converted.getClasses().isEmpty());
    }

    private YearEntity createYearEntity(final long id, final String name, final boolean running) {
        final YearEntity yearEntity = new YearEntity();
        ReflectionUtils.setField(yearEntity, "id", Long.valueOf(id));
        yearEntity.setName(name);
        yearEntity.setRunning(running);
        yearEntity.setClasses(Arrays.asList(createSimpleClass(3L), createSimpleClass(4L)));

        return yearEntity;
    }

    private ClazzEntity createSimpleClass(final long id) {
        final ClazzEntity clazzEntity = new ClazzEntity();
        ReflectionUtils.setField(clazzEntity, "id", Long.valueOf(id));

        return clazzEntity;
    }
}
