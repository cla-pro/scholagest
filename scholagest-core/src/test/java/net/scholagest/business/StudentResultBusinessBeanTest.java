package net.scholagest.business;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import net.scholagest.ReflectionUtils;
import net.scholagest.dao.StudentResultDaoLocal;
import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.db.entity.MeanEntity;
import net.scholagest.db.entity.ResultEntity;
import net.scholagest.db.entity.StudentEntity;
import net.scholagest.db.entity.StudentResultEntity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Teacher class for {@link StudentResultBusinessBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
@RunWith(MockitoJUnitRunner.class)
public class StudentResultBusinessBeanTest {
    @Mock
    private StudentResultDaoLocal studentResultDao;

    @InjectMocks
    private final StudentResultBusinessLocal testee = new StudentResultBusinessBean();

    @Test
    public void testGetStudentResult() {
        final long id = 1L;
        final StudentResultEntity branchPeriodEntity = createStudentResultEntity(id, true);
        when(studentResultDao.getStudentResultEntityById(eq(id))).thenReturn(branchPeriodEntity);

        assertNull(testee.getStudentResult(2L));
        verify(studentResultDao).getStudentResultEntityById(eq(2L));

        assertNotNull(testee.getStudentResult(id));
        verify(studentResultDao).getStudentResultEntityById(eq(id));
    }

    private StudentResultEntity createStudentResultEntity(final Long id, final boolean active) {
        final StudentResultEntity studentResultEntity = new StudentResultEntity();
        ReflectionUtils.setField(studentResultEntity, "id", id);
        studentResultEntity.setActive(active);
        studentResultEntity.setBranchPeriod(new BranchPeriodEntity());
        studentResultEntity.setMean(new MeanEntity());
        studentResultEntity.setResults(new ArrayList<ResultEntity>());
        studentResultEntity.setStudent(new StudentEntity());

        return studentResultEntity;
    }
}
