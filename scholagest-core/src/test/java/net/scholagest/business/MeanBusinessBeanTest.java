package net.scholagest.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.scholagest.ReflectionUtils;
import net.scholagest.dao.MeanDaoLocal;
import net.scholagest.db.entity.MeanEntity;
import net.scholagest.db.entity.StudentResultEntity;
import net.scholagest.object.Mean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Teacher class for {@link MeanBusinessBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
@RunWith(MockitoJUnitRunner.class)
public class MeanBusinessBeanTest {
    @Mock
    private MeanDaoLocal meanDao;

    @InjectMocks
    private final MeanBusinessLocal testee = new MeanBusinessBean();

    @Test
    public void testGetMean() {
        final long id = 1;
        final MeanEntity meanEntity = createMeanEntity(1L, "5.6");
        when(meanDao.getMeanEntityById(eq(id))).thenReturn(meanEntity);

        assertNull(testee.getMean(2L));
        verify(meanDao).getMeanEntityById(eq(2L));

        assertNotNull(testee.getMean(id));
        verify(meanDao).getMeanEntityById(eq(id));
    }

    @Test
    public void testSaveMean() {
        final Mean mean = new Mean("2", "3.5", "studentResult");
        final MeanEntity meanEntityMock = createMeanEntity(2L, "3.4");

        when(meanDao.getMeanEntityById(eq(2L))).thenReturn(meanEntityMock);

        final Mean saved = testee.saveMean(mean);

        assertEquals(mean.getGrade(), saved.getGrade());
    }

    private MeanEntity createMeanEntity(final Long id, final String grade) {
        final MeanEntity meanEntity = new MeanEntity();
        ReflectionUtils.setField(meanEntity, "id", id);
        meanEntity.setGrade(grade);
        meanEntity.setStudentResult(new StudentResultEntity());

        return meanEntity;
    }
}
