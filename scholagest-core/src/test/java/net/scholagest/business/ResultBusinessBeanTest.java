package net.scholagest.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.scholagest.ReflectionUtils;
import net.scholagest.dao.ResultDaoLocal;
import net.scholagest.db.entity.ExamEntity;
import net.scholagest.db.entity.ResultEntity;
import net.scholagest.db.entity.StudentResultEntity;
import net.scholagest.object.Result;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Teacher class for {@link ResultBusinessBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
@RunWith(MockitoJUnitRunner.class)
public class ResultBusinessBeanTest {
    @Mock
    private ResultDaoLocal resultDao;

    @InjectMocks
    private final ResultBusinessLocal testee = new ResultBusinessBean();

    @Test
    public void testGetResult() {
        final long id = 1;
        final ResultEntity resultEntity = createResultEntity(1L, "5.6");
        when(resultDao.getResultEntityById(eq(id))).thenReturn(resultEntity);

        assertNull(testee.getResult(2L));
        verify(resultDao).getResultEntityById(eq(2L));

        assertNotNull(testee.getResult(id));
        verify(resultDao).getResultEntityById(eq(id));
    }

    @Test
    public void testSaveResult() {
        final Result result = new Result("2", "3.5", "exam", "studentResult");
        final ResultEntity resultEntityMock = createResultEntity(2L, "3.4");

        when(resultDao.getResultEntityById(eq(2L))).thenReturn(resultEntityMock);

        final Result saved = testee.saveResult(result);

        assertEquals(result.getGrade(), saved.getGrade());
    }

    private ResultEntity createResultEntity(final Long id, final String grade) {
        final ResultEntity resultEntity = new ResultEntity();
        ReflectionUtils.setField(resultEntity, "id", id);
        resultEntity.setGrade(grade);
        resultEntity.setExam(new ExamEntity());
        resultEntity.setStudentResult(new StudentResultEntity());

        return resultEntity;
    }
}
