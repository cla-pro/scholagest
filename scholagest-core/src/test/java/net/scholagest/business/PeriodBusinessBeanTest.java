package net.scholagest.business;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import net.scholagest.ReflectionUtils;
import net.scholagest.dao.PeriodDaoLocal;
import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.db.entity.ClazzEntity;
import net.scholagest.db.entity.PeriodEntity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Teacher class for {@link PeriodBusinessBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
@RunWith(MockitoJUnitRunner.class)
public class PeriodBusinessBeanTest {
    @Mock
    private PeriodDaoLocal periodDao;

    @InjectMocks
    private final PeriodBusinessLocal testee = new PeriodBusinessBean();

    @Test
    public void testGetPeriod() {
        final long id = 1L;
        final PeriodEntity periodEntity = createPeriodEntity(id, "name");
        when(periodDao.getPeriodEntityById(eq(id))).thenReturn(periodEntity);

        assertNull(testee.getPeriod(2L));
        verify(periodDao).getPeriodEntityById(eq(2L));

        assertNotNull(testee.getPeriod(id));
        verify(periodDao).getPeriodEntityById(eq(id));
    }

    private PeriodEntity createPeriodEntity(final Long id, final String name) {
        final PeriodEntity periodEntity = new PeriodEntity();
        ReflectionUtils.setField(periodEntity, "id", id);
        periodEntity.setName(name);
        periodEntity.setClazz(new ClazzEntity());
        periodEntity.setBranchPeriods(new ArrayList<BranchPeriodEntity>());

        return periodEntity;
    }
}
