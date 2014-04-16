package net.scholagest.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import net.scholagest.ReflectionUtils;
import net.scholagest.dao.ClazzDaoLocal;
import net.scholagest.dao.PeriodDaoLocal;
import net.scholagest.dao.YearDaoLocal;
import net.scholagest.db.entity.BranchEntity;
import net.scholagest.db.entity.ClazzEntity;
import net.scholagest.db.entity.PeriodEntity;
import net.scholagest.db.entity.YearEntity;
import net.scholagest.object.Clazz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Teacher class for {@link ClazzBusinessBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
@RunWith(MockitoJUnitRunner.class)
public class ClazzBusinessBeanTest {
    @Mock
    private ClazzDaoLocal clazzDao;

    @Mock
    private PeriodDaoLocal periodDao;

    @Mock
    private YearDaoLocal yearDao;

    @InjectMocks
    private final ClazzBusinessLocal testee = new ClazzBusinessBean();

    @Test
    public void testGetClazz() {
        final long id = 1;
        final ClazzEntity clazzEntity = createClazzEntity(id, "name");
        when(clazzDao.getClazzEntityById(eq(id))).thenReturn(clazzEntity);

        assertNull(testee.getClazz(2L));
        verify(clazzDao).getClazzEntityById(eq(2L));

        assertNotNull(testee.getClazz(id));
        verify(clazzDao).getClazzEntityById(eq(id));
    }

    @Test
    public void testCreateClazz() {
        final Clazz clazz = new Clazz("2", "name", "2", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(),
                new ArrayList<String>());
        final ClazzEntity clazzEntityMock = createClazzEntity(2L, "name");

        when(clazzDao.persistClazzEntity(any(ClazzEntity.class))).thenReturn(clazzEntityMock);
        when(periodDao.persistPeriodEntity(any(PeriodEntity.class))).thenReturn(new PeriodEntity());

        final Clazz result = testee.createClazz(clazz);

        assertEquals(clazz, result);
        final ArgumentCaptor<ClazzEntity> teacherCaptor = ArgumentCaptor.forClass(ClazzEntity.class);
        verify(clazzDao).persistClazzEntity(teacherCaptor.capture());
        assertNull(teacherCaptor.getValue().getId());

        verify(periodDao, times(3)).persistPeriodEntity(any(PeriodEntity.class));
        verify(yearDao).getYearEntityById(Long.valueOf(clazz.getYear()));
    }

    @Test
    public void testSaveClazz() {
        final Clazz clazz = new Clazz("2", "name2", "year", Arrays.asList("period1", "period2"), Arrays.asList("teacher1", "teacher2"),
                Arrays.asList("student1", "student2"), Arrays.asList("branch1", "branch2"));
        final ClazzEntity clazzEntityMock = createClazzEntity(2L, "name");

        when(clazzDao.getClazzEntityById(eq(2L))).thenReturn(clazzEntityMock);

        final Clazz result = testee.saveClazz(clazz);

        assertEquals(clazz.getName(), result.getName());
    }

    private ClazzEntity createClazzEntity(final Long id, final String name) {
        final ClazzEntity clazzEntity = new ClazzEntity();
        ReflectionUtils.setField(clazzEntity, "id", id);
        clazzEntity.setName(name);
        clazzEntity.setYear(new YearEntity());
        clazzEntity.setBranches(new ArrayList<BranchEntity>());
        clazzEntity.setPeriods(new ArrayList<PeriodEntity>());

        return clazzEntity;
    }
}
