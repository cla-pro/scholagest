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
import net.scholagest.db.entity.BranchEntity;
import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.db.entity.ClazzEntity;
import net.scholagest.object.Branch;

import org.junit.Test;

/**
 * Test class for {@link BranchEntityConverter}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class BranchEntityConverterTest {
    @Test
    public void testConvertToBranchList() {
        final BranchEntity branchEntity1 = createBranchEntity(1L, "name1", true);
        final BranchEntity branchEntity2 = createBranchEntity(2L, "name2", false);
        final List<BranchEntity> toConvert = Arrays.asList(branchEntity1, branchEntity2);

        final BranchEntityConverter testee = spy(new BranchEntityConverter());
        final List<Branch> converted = testee.convertToBranchList(toConvert);

        assertEquals(toConvert.size(), converted.size());
        for (final BranchEntity branchEntity : toConvert) {
            verify(testee).convertToBranch(eq(branchEntity));
        }
    }

    @Test
    public void testConvertToBranch() {
        final BranchEntity branchEntity = createBranchEntity(1L, "name", true);

        final BranchEntityConverter testee = spy(new BranchEntityConverter());
        final Branch converted = testee.convertToBranch(branchEntity);

        assertEquals(branchEntity.getId().toString(), converted.getId());
        assertEquals(branchEntity.getName(), converted.getName());
        assertEquals("" + branchEntity.getClazz().getId(), converted.getClazz());

        for (final BranchPeriodEntity branchPeriodEntity : branchEntity.getBranchPeriods()) {
            assertTrue(converted.getBranchPeriods().contains("" + branchPeriodEntity.getId()));
        }
    }

    @Test
    public void testConvertToBranchEntity() {
        final Branch clazz = new Branch("3", "name", true, "clazz", Arrays.asList("1", "2"));

        final BranchEntityConverter testee = spy(new BranchEntityConverter());
        final BranchEntity converted = testee.convertToBranchEntity(clazz);

        assertNull(converted.getId());
        assertEquals(clazz.getName(), converted.getName());
        assertEquals(clazz.isNumerical(), converted.isNumerical());
        assertNull(converted.getClazz());
        assertTrue(converted.getBranchPeriods().isEmpty());
    }

    private BranchEntity createBranchEntity(final long id, final String name, final boolean numerical) {
        final BranchEntity branchEntity = new BranchEntity();
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
