package net.scholagest.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import net.scholagest.ReflectionUtils;
import net.scholagest.db.entity.ClazzEntity;
import net.scholagest.db.entity.YearEntity;
import net.scholagest.object.Clazz;

import org.junit.Test;

/**
 * Test class for {@link ClazzEntityConverter}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class ClazzEntityConverterTest {
    @Test
    public void testConvertToClazzList() {
        final ClazzEntity clazzEntity1 = createClazzEntity(1L, "name1");
        final ClazzEntity clazzEntity2 = createClazzEntity(2L, "name2");
        final List<ClazzEntity> toConvert = Arrays.asList(clazzEntity1, clazzEntity2);

        final ClazzEntityConverter testee = spy(new ClazzEntityConverter());
        final List<Clazz> converted = testee.convertToClazzList(toConvert);

        assertEquals(toConvert.size(), converted.size());
        for (final ClazzEntity clazzEntity : toConvert) {
            verify(testee).convertToClazz(eq(clazzEntity));
        }
    }

    @Test
    public void testConvertToClazz() {
        final ClazzEntity clazzEntity = createClazzEntity(1L, "name");

        final ClazzEntityConverter testee = spy(new ClazzEntityConverter());
        final Clazz converted = testee.convertToClazz(clazzEntity);

        assertEquals(clazzEntity.getId().toString(), converted.getId());
        assertEquals(clazzEntity.getName(), converted.getName());
        assertEquals("" + clazzEntity.getYear().getId(), converted.getYear());
    }

    @Test
    public void testConvertToClazzEntity() {
        final Clazz clazz = new Clazz("3", "name", "year", Arrays.asList("1", "2"), Arrays.asList("3", "4"), Arrays.asList("5", "6"), Arrays.asList(
                "7", "8"));

        final ClazzEntityConverter testee = spy(new ClazzEntityConverter());
        final ClazzEntity converted = testee.convertToClazzEntity(clazz);

        assertNull(converted.getId());
        assertEquals(clazz.getName(), converted.getName());
        assertNull(converted.getYear());
    }

    private ClazzEntity createClazzEntity(final long id, final String name) {
        final ClazzEntity clazzEntity = new ClazzEntity();
        ReflectionUtils.setField(clazzEntity, "id", Long.valueOf(id));
        clazzEntity.setName(name);
        clazzEntity.setYear(createSimpleYearEntity(3L));

        return clazzEntity;
    }

    private YearEntity createSimpleYearEntity(final long id) {
        final YearEntity yearEntity = new YearEntity();
        ReflectionUtils.setField(yearEntity, "id", Long.valueOf(id));

        return yearEntity;
    }
}
