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
import java.util.List;

import net.scholagest.ReflectionUtils;
import net.scholagest.dao.BranchDaoLocal;
import net.scholagest.dao.BranchPeriodDaoLocal;
import net.scholagest.dao.ClazzDaoLocal;
import net.scholagest.dao.StudentResultDaoLocal;
import net.scholagest.db.entity.BranchEntity;
import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.db.entity.ClazzEntity;
import net.scholagest.db.entity.PeriodEntity;
import net.scholagest.db.entity.StudentEntity;
import net.scholagest.db.entity.StudentResultEntity;
import net.scholagest.object.Branch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Teacher class for {@link BranchBusinessBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
@RunWith(MockitoJUnitRunner.class)
public class BranchBusinessBeanTest {
    @Mock
    private BranchDaoLocal branchDao;

    @Mock
    private ClazzDaoLocal clazzDao;

    @Mock
    private BranchPeriodDaoLocal branchPeriodDao;

    @Mock
    private StudentResultDaoLocal studentResultDao;

    @InjectMocks
    private final BranchBusinessLocal testee = new BranchBusinessBean();

    @Test
    public void testGetBranch() {
        final long id = 1;
        final BranchEntity branchEntity = createBranchEntity(1L, "name");
        when(branchDao.getBranchEntityById(eq(id))).thenReturn(branchEntity);

        assertNull(testee.getBranch(2L));
        verify(branchDao).getBranchEntityById(eq(2L));

        assertNotNull(testee.getBranch(id));
        verify(branchDao).getBranchEntityById(eq(id));
    }

    @Test
    public void testCreateBranch() {
        final Branch branch = new Branch("2", "name", false, "3", new ArrayList<String>());
        final BranchEntity branchEntityMock = createBranchEntity(2L, "name");

        when(branchDao.persistBranchEntity(any(BranchEntity.class))).thenReturn(branchEntityMock);
        when(clazzDao.getClazzEntityById(eq(Long.valueOf(branch.getClazz())))).thenReturn(createClazzEntity(3L));

        final Branch result = testee.createBranch(branch);

        assertEquals(branch, result);
        final ArgumentCaptor<BranchEntity> teacherCaptor = ArgumentCaptor.forClass(BranchEntity.class);
        verify(branchDao).persistBranchEntity(teacherCaptor.capture());
        assertNull(teacherCaptor.getValue().getId());

        verify(branchPeriodDao, times(3)).persistBranchPeriodEntity(any(BranchPeriodEntity.class));
        verify(studentResultDao, times(6)).persistStudentResultEntity(any(StudentResultEntity.class));
    }

    @Test
    public void testSaveBranch() {
        final Branch branch = new Branch("2", "name2", true, "clazz", new ArrayList<String>());
        final BranchEntity branchEntityMock = createBranchEntity(2L, "name");

        when(branchDao.getBranchEntityById(eq(2L))).thenReturn(branchEntityMock);

        final Branch result = testee.saveBranch(branch);

        assertEquals(branch.getName(), result.getName());
    }

    private BranchEntity createBranchEntity(final Long id, final String name) {
        final BranchEntity branchEntity = new BranchEntity();
        ReflectionUtils.setField(branchEntity, "id", id);
        branchEntity.setName(name);
        branchEntity.setBranchPeriods(new ArrayList<BranchPeriodEntity>());
        branchEntity.setClazz(new ClazzEntity());

        return branchEntity;
    }

    private ClazzEntity createClazzEntity(final long id) {
        final ClazzEntity clazzEntity = new ClazzEntity();
        ReflectionUtils.setField(clazzEntity, "id", Long.valueOf(id));
        clazzEntity.setPeriods(createPeriodEntityList(clazzEntity));
        clazzEntity.setStudents(Arrays.asList(createSimpleStudentEntity(3L), createSimpleStudentEntity(4L)));

        return clazzEntity;
    }

    private StudentEntity createSimpleStudentEntity(final long id) {
        final StudentEntity studentEntity = new StudentEntity();
        ReflectionUtils.setField(studentEntity, "id", id);

        return studentEntity;
    }

    private List<PeriodEntity> createPeriodEntityList(final ClazzEntity clazzEntity) {
        final List<PeriodEntity> periodEntityList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final PeriodEntity periodEntity = new PeriodEntity();
            periodEntity.setClazz(clazzEntity);
            periodEntityList.add(periodEntity);
        }

        return periodEntityList;
    }
}
