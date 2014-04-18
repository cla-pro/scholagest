package net.scholagest.business;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import net.scholagest.ReflectionUtils;
import net.scholagest.dao.BranchPeriodDaoLocal;
import net.scholagest.db.entity.BranchEntity;
import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.db.entity.ExamEntity;
import net.scholagest.db.entity.PeriodEntity;
import net.scholagest.db.entity.StudentResultEntity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Teacher class for {@link BranchPeriodBusinessBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
@RunWith(MockitoJUnitRunner.class)
public class BranchPeriodBusinessBeanTest {
    @Mock
    private BranchPeriodDaoLocal branchPeriodDao;

    @InjectMocks
    private final BranchPeriodBusinessLocal testee = new BranchPeriodBusinessBean();

    @Test
    public void testGetBranchPeriod() {
        final long id = 1L;
        final BranchPeriodEntity branchPeriodEntity = createBranchPeriodEntity(id);
        when(branchPeriodDao.getBranchPeriodEntityById(eq(id))).thenReturn(branchPeriodEntity);

        assertNull(testee.getBranchPeriod(2L));
        verify(branchPeriodDao).getBranchPeriodEntityById(eq(2L));

        assertNotNull(testee.getBranchPeriod(id));
        verify(branchPeriodDao).getBranchPeriodEntityById(eq(id));
    }

    private BranchPeriodEntity createBranchPeriodEntity(final Long id) {
        final BranchPeriodEntity branchPeriodEntity = new BranchPeriodEntity();
        ReflectionUtils.setField(branchPeriodEntity, "id", id);
        branchPeriodEntity.setBranch(new BranchEntity());
        branchPeriodEntity.setExams(new ArrayList<ExamEntity>());
        branchPeriodEntity.setPeriod(new PeriodEntity());
        branchPeriodEntity.setStudentResults(new ArrayList<StudentResultEntity>());

        return branchPeriodEntity;
    }
}
