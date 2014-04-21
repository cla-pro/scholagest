package net.scholagest.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import net.scholagest.ReflectionUtils;
import net.scholagest.dao.ClazzDaoLocal;
import net.scholagest.dao.MeanDaoLocal;
import net.scholagest.dao.PeriodDaoLocal;
import net.scholagest.dao.ResultDaoLocal;
import net.scholagest.dao.StudentDaoLocal;
import net.scholagest.dao.StudentResultDaoLocal;
import net.scholagest.dao.TeacherDaoLocal;
import net.scholagest.dao.YearDaoLocal;
import net.scholagest.db.entity.BranchEntity;
import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.db.entity.ClazzEntity;
import net.scholagest.db.entity.ExamEntity;
import net.scholagest.db.entity.MeanEntity;
import net.scholagest.db.entity.PeriodEntity;
import net.scholagest.db.entity.ResultEntity;
import net.scholagest.db.entity.StudentEntity;
import net.scholagest.db.entity.StudentResultEntity;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.db.entity.YearEntity;
import net.scholagest.object.Clazz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

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

    @Mock
    private TeacherDaoLocal teacherDao;

    @Mock
    private StudentDaoLocal studentDao;

    @Mock
    private StudentResultDaoLocal studentResultDao;

    @Mock
    private ResultDaoLocal resultDao;

    @Mock
    private MeanDaoLocal meanDao;

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
        final Clazz clazz = new Clazz("2", "name2", "year", Arrays.asList("1", "2"), Arrays.asList("3", "4"), Arrays.asList("5", "6"), Arrays.asList(
                "7", "8"));
        final ClazzEntity clazzEntityMock = createClazzEntity(2L, "name");

        when(clazzDao.getClazzEntityById(eq(2L))).thenReturn(clazzEntityMock);
        when(teacherDao.getTeacherEntityById(anyLong())).thenAnswer(new Answer<TeacherEntity>() {
            @Override
            public TeacherEntity answer(final InvocationOnMock invocation) throws Throwable {
                final Long id = (Long) invocation.getArguments()[0];
                final TeacherEntity entity = new TeacherEntity();
                ReflectionUtils.setField(entity, "id", id);
                return entity;
            }
        });
        when(studentDao.getStudentEntityById(anyLong())).thenAnswer(new Answer<StudentEntity>() {
            @Override
            public StudentEntity answer(final InvocationOnMock invocation) throws Throwable {
                final Long id = (Long) invocation.getArguments()[0];
                final StudentEntity entity = new StudentEntity();
                ReflectionUtils.setField(entity, "id", id);
                return entity;
            }
        });
        when(studentResultDao.persistStudentResultEntity(any(StudentResultEntity.class))).thenAnswer(new Answer<StudentResultEntity>() {
            @Override
            public StudentResultEntity answer(final InvocationOnMock invocation) throws Throwable {
                return (StudentResultEntity) invocation.getArguments()[0];
            }
        });
        when(resultDao.persistResultEntity(any(ResultEntity.class))).thenAnswer(new Answer<ResultEntity>() {
            @Override
            public ResultEntity answer(final InvocationOnMock invocation) throws Throwable {
                return (ResultEntity) invocation.getArguments()[0];
            }
        });

        final Clazz result = testee.saveClazz(clazz);

        assertEquals(clazz.getName(), result.getName());
        assertEquals(clazz.getTeachers(), result.getTeachers());
        // assertEquals(clazz.getStudents(), result.getStudents());

        verify(teacherDao, times(2)).getTeacherEntityById(anyLong());
        verify(studentDao, times(2)).getStudentEntityById(anyLong());
        // times: 2 periods x 2 branches x 2 students
        verify(studentResultDao, times(8)).persistStudentResultEntity(any(StudentResultEntity.class));
        verify(resultDao, times(8)).persistResultEntity(any(ResultEntity.class));
        verify(meanDao, times(8)).persistMeanEntity(any(MeanEntity.class));
    }

    private ClazzEntity createClazzEntity(final Long id, final String name) {
        final ClazzEntity clazzEntity = new ClazzEntity();
        ReflectionUtils.setField(clazzEntity, "id", id);
        clazzEntity.setName(name);
        clazzEntity.setYear(new YearEntity());
        clazzEntity.setBranches(Arrays.asList(createSimpleBranchEntity(7L), createSimpleBranchEntity(8L)));
        clazzEntity.setPeriods(Arrays.asList(createSimplePeriodEntity(1L), createSimplePeriodEntity(2L)));
        clazzEntity.setTeachers(new ArrayList<TeacherEntity>());
        clazzEntity.setStudents(new ArrayList<StudentEntity>());

        return clazzEntity;
    }

    private BranchEntity createSimpleBranchEntity(final long id) {
        final BranchEntity branchEntity = new BranchEntity();
        ReflectionUtils.setField(branchEntity, "id", Long.valueOf(id));
        return branchEntity;
    }

    private PeriodEntity createSimplePeriodEntity(final long id) {
        final PeriodEntity periodEntity = new PeriodEntity();
        ReflectionUtils.setField(periodEntity, "id", Long.valueOf(id));
        periodEntity.setBranchPeriods(Arrays.asList(createSimpleBranchPeriodEntity(10L), createSimpleBranchPeriodEntity(11L)));
        return periodEntity;
    }

    private BranchPeriodEntity createSimpleBranchPeriodEntity(final long id) {
        final BranchPeriodEntity branchPeriodEntity = new BranchPeriodEntity();
        ReflectionUtils.setField(branchPeriodEntity, "id", Long.valueOf(id));
        branchPeriodEntity.setExams(Arrays.asList(createSimpleExamEntity(21L)));
        return branchPeriodEntity;
    }

    private ExamEntity createSimpleExamEntity(final long id) {
        final ExamEntity examEntity = new ExamEntity();
        ReflectionUtils.setField(examEntity, "id", Long.valueOf(id));
        return examEntity;
    }
}
