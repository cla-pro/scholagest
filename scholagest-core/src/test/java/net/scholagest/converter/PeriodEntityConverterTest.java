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
import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.db.entity.ClazzEntity;
import net.scholagest.db.entity.PeriodEntity;
import net.scholagest.object.Period;

import org.junit.Test;

/**
 * Test class for {@link PeriodEntityConverter}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class PeriodEntityConverterTest {
    @Test
    public void testConvertToPeriodList() {
        final PeriodEntity branchEntity1 = createPeriodEntity(1L, "name1", true);
        final PeriodEntity branchEntity2 = createPeriodEntity(2L, "name2", false);
        final List<PeriodEntity> toConvert = Arrays.asList(branchEntity1, branchEntity2);

        final PeriodEntityConverter testee = spy(new PeriodEntityConverter());
        final List<Period> converted = testee.convertToPeriodList(toConvert);

        assertEquals(toConvert.size(), converted.size());
        for (final PeriodEntity branchEntity : toConvert) {
            verify(testee).convertToPeriod(eq(branchEntity));
        }
    }

    @Test
    public void testConvertToPeriod() {
        final PeriodEntity branchEntity = createPeriodEntity(1L, "name", true);

        final PeriodEntityConverter testee = spy(new PeriodEntityConverter());
        final Period converted = testee.convertToPeriod(branchEntity);

        assertEquals(branchEntity.getId().toString(), converted.getId());
        assertEquals(branchEntity.getName(), converted.getName());
        assertEquals("" + branchEntity.getClazz().getId(), converted.getClazz());

        for (final BranchPeriodEntity branchPeriodEntity : branchEntity.getBranchPeriods()) {
            assertTrue(converted.getBranchPeriods().contains("" + branchPeriodEntity.getId()));
        }
    }

    @Test
    public void testConvertToPeriodEntity() {
        final Period clazz = new Period("3", "name", "clazz", Arrays.asList("1", "2"));

        final PeriodEntityConverter testee = spy(new PeriodEntityConverter());
        final PeriodEntity converted = testee.convertToPeriodEntity(clazz);

        assertNull(converted.getId());
        assertEquals(clazz.getName(), converted.getName());
        assertNull(converted.getClazz());
        assertTrue(converted.getBranchPeriods().isEmpty());
    }

    private PeriodEntity createPeriodEntity(final long id, final String name, final boolean numerical) {
        final PeriodEntity branchEntity = new PeriodEntity();
        ReflectionUtils.setField(branchEntity, "id", Long.valueOf(id));
        branchEntity.setName(name);
        branchEntity.setClazz(createSimpleClazzEntity(3L));
        branchEntity.setBranchPeriods(Arrays.asList(createSimpleBranchPeriodEntity(4L), createSimpleBranchPeriodEntity(5L)));

        return branchEntity;
    }

    private BranchPeriodEntity createSimpleBranchPeriodEntity(final long id) {
        final BranchPeriodEntity branchPeriodEntity = new BranchPeriodEntity();
        ReflectionUtils.setField(branchPeriodEntity, "id", Long.valueOf(id));

        return branchPeriodEntity;
    }

    private ClazzEntity createSimpleClazzEntity(final long id) {
        final ClazzEntity clazzEntity = new ClazzEntity();
        ReflectionUtils.setField(clazzEntity, "id", Long.valueOf(id));

        return clazzEntity;
    }
}
