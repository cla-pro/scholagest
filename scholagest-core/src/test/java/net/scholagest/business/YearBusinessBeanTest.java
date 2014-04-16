package net.scholagest.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.scholagest.ReflectionUtils;
import net.scholagest.dao.YearDaoLocal;
import net.scholagest.db.entity.ClazzEntity;
import net.scholagest.db.entity.YearEntity;
import net.scholagest.object.Year;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Teacher class for {@link YearBusinessBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
@RunWith(MockitoJUnitRunner.class)
public class YearBusinessBeanTest {
    @Mock
    private YearDaoLocal yearDao;

    @InjectMocks
    private final YearBusinessLocal testee = new YearBusinessBean();

    @Test
    public void testGetYears() {
        final YearEntity yearEntity1 = createYearEntity(1L, "name1", true);
        final YearEntity yearEntity2 = createYearEntity(2L, "name2", false);
        final List<YearEntity> yearEntityList = Arrays.asList(yearEntity1, yearEntity2);
        when(yearDao.getAllYearEntity()).thenReturn(yearEntityList);

        final List<Year> result = testee.getYears();
        assertEquals(yearEntityList.size(), result.size());

        verify(yearDao).getAllYearEntity();
    }

    @Test
    public void testGetYear() {
        final long id = 1;
        final YearEntity yearEntity = createYearEntity(1L, "name", true);
        when(yearDao.getYearEntityById(eq(id))).thenReturn(yearEntity);

        assertNull(testee.getYear(2L));
        verify(yearDao).getYearEntityById(eq(2L));

        assertNotNull(testee.getYear(id));
        verify(yearDao).getYearEntityById(eq(id));
    }

    @Test
    public void testCreateYear() {
        final Year year = new Year("2", "name", true, new ArrayList<String>());
        final YearEntity yearEntityMock = createYearEntity(2L, "name", true);

        when(yearDao.persistYearEntity(any(YearEntity.class))).thenReturn(yearEntityMock);

        final Year result = testee.createYear(year);

        assertEquals(year, result);
        final ArgumentCaptor<YearEntity> teacherCaptor = ArgumentCaptor.forClass(YearEntity.class);
        verify(yearDao).persistYearEntity(teacherCaptor.capture());
        assertNull(teacherCaptor.getValue().getId());
    }

    @Test
    public void testSaveYear() {
        final Year year = new Year("2", "name2", false, Arrays.asList("clazz1", "clazz2"));
        final YearEntity yearEntityMock = createYearEntity(2L, "name", true);

        when(yearDao.getYearEntityById(eq(2L))).thenReturn(yearEntityMock);

        final Year result = testee.saveYear(year);

        assertEquals(year.getName(), result.getName());
        assertEquals(year.isRunning(), result.isRunning());
    }

    private YearEntity createYearEntity(final Long id, final String name, final boolean running) {
        final YearEntity yearEntity = new YearEntity();
        ReflectionUtils.setField(yearEntity, "id", id);
        yearEntity.setName(name);
        yearEntity.setRunning(running);
        yearEntity.setClasses(new ArrayList<ClazzEntity>());

        return yearEntity;
    }
}
