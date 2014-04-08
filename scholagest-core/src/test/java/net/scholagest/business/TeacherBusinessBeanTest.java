package net.scholagest.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.scholagest.ReflectionUtils;
import net.scholagest.dao.TeacherDaoLocal;
import net.scholagest.db.entity.TeacherDetailEntity;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.object.Teacher;
import net.scholagest.object.TeacherDetail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Teacher class for {@link TeacherBusinessBean}
 * 
 * @author CLA
 * @since 0.15.0
 */
@RunWith(MockitoJUnitRunner.class)
public class TeacherBusinessBeanTest {
    @Mock
    private TeacherDaoLocal teacherDao;

    @InjectMocks
    private final TeacherBusinessLocal testee = new TeacherBusinessBean();

    @Test
    public void testGetTeachers() {
        final TeacherEntity teacherEntity1 = createTeacherEntity(1L, "firstname1", "lastname1");
        final TeacherEntity teacherEntity2 = createTeacherEntity(2L, "firstname2", "lastname2");
        final List<TeacherEntity> teacherEntityList = Arrays.asList(teacherEntity1, teacherEntity2);
        when(teacherDao.getAllTeacherEntity()).thenReturn(teacherEntityList);

        final List<Teacher> result = testee.getTeachers();
        assertEquals(teacherEntityList.size(), result.size());

        verify(teacherDao).getAllTeacherEntity();
    }

    @Test
    public void testGetTeachersEmpty() {
        when(teacherDao.getAllTeacherEntity()).thenReturn(new ArrayList<TeacherEntity>());

        assertTrue(testee.getTeachers().isEmpty());

        verify(teacherDao).getAllTeacherEntity();
    }

    @Test
    public void testGetTeacher() {
        final long id = 1;
        final TeacherEntity teacherEntity = createTeacherEntity(1L, "firstname", "lastname");
        when(teacherDao.getTeacherEntityById(eq(id))).thenReturn(teacherEntity);

        assertNotNull(testee.getTeacher(id));
        assertNull(testee.getTeacher(2L));

        verify(teacherDao).getTeacherEntityById(eq(id));
        verify(teacherDao).getTeacherEntityById(eq(2L));
    }

    @Test
    public void testCreateTeacher() {
        final Teacher teacher = new Teacher("2", "firstname", "lastname", new TeacherDetail("3", null, null, null));
        final TeacherEntity teacherEntityMock = new TeacherEntity();
        final TeacherDetailEntity teacherDetailEntityMock = new TeacherDetailEntity();
        ReflectionUtils.setField(teacherEntityMock, "id", Long.valueOf(2L));
        teacherEntityMock.setFirstname("firstname");
        teacherEntityMock.setLastname("lastname");
        teacherEntityMock.setTeacherDetail(teacherDetailEntityMock);
        ReflectionUtils.setField(teacherDetailEntityMock, "id", Long.valueOf(3L));

        when(teacherDao.persistTeacherEntity(any(TeacherEntity.class))).thenReturn(teacherEntityMock);

        final Teacher result = testee.createTeacher(teacher);

        assertEquals(teacher, result);
        final ArgumentCaptor<TeacherEntity> argumentCaptor = ArgumentCaptor.forClass(TeacherEntity.class);
        verify(teacherDao).persistTeacherEntity(argumentCaptor.capture());
        assertNull(argumentCaptor.getValue().getId());
        assertNull(argumentCaptor.getValue().getTeacherDetail().getId());
    }

    @Test
    public void testSaveTeacher() {
        final Teacher teacher = new Teacher("2", "firstname2", "lastname2", new TeacherDetail("3", "shouldNotBeSaved", "shouldNotBeSaved",
                "shouldNotBeSaved"));
        final TeacherEntity teacherEntityMock = new TeacherEntity();
        final TeacherDetailEntity teacherDetailEntityMock = new TeacherDetailEntity();
        ReflectionUtils.setField(teacherEntityMock, "id", Long.valueOf(2L));
        teacherEntityMock.setFirstname("firstname");
        teacherEntityMock.setLastname("lastname");
        teacherEntityMock.setTeacherDetail(teacherDetailEntityMock);
        ReflectionUtils.setField(teacherDetailEntityMock, "id", Long.valueOf(3L));

        when(teacherDao.getTeacherEntityById(eq(2L))).thenReturn(teacherEntityMock);

        final Teacher result = testee.saveTeacher(teacher);

        assertEquals(teacher.getFirstname(), result.getFirstname());
        assertEquals(teacher.getLastname(), result.getLastname());
        assertNull(result.getDetail().getAddress());
        assertNull(result.getDetail().getEmail());
        assertNull(result.getDetail().getPhone());
    }

    @Test
    public void testSaveTeacherNonExisting() {
        final Teacher nonExistingTeacher = new Teacher("3", "firstname2", "lastname2", new TeacherDetail("3", "shouldNotBeSaved", "shouldNotBeSaved",
                "shouldNotBeSaved"));
        assertNull(testee.saveTeacher(nonExistingTeacher));
    }

    @Test
    public void testGetTeacherDetail() {
        final TeacherDetail expected = new TeacherDetail("3", "address", "email", "phone");

        final TeacherEntity teacherEntityMock = new TeacherEntity();
        final TeacherDetailEntity teacherDetailEntityMock = new TeacherDetailEntity();
        ReflectionUtils.setField(teacherEntityMock, "id", Long.valueOf(2L));
        teacherEntityMock.setFirstname("firstname");
        teacherEntityMock.setLastname("lastname");
        teacherEntityMock.setTeacherDetail(teacherDetailEntityMock);
        ReflectionUtils.setField(teacherDetailEntityMock, "id", Long.valueOf(3L));
        teacherDetailEntityMock.setAddress("address");
        teacherDetailEntityMock.setEmail("email");
        teacherDetailEntityMock.setPhone("phone");

        when(teacherDao.getTeacherDetailEntityById(eq(3L))).thenReturn(teacherEntityMock.getTeacherDetail());

        final TeacherDetail teacherDetail = testee.getTeacherDetail(3L);
        assertEquals(expected, teacherDetail);
    }

    @Test
    public void testGetTeacherDetailNonExisting() {
        assertNull(testee.getTeacherDetail(4L));
    }

    @Test
    public void saveTeacherDetail() {
        final TeacherDetail teacherDetail = new TeacherDetail("3", "address2", "phone2", "email2");
        final Teacher teacher = new Teacher("2", "shouldNotBeSaved", "shouldNotBeSaved", teacherDetail);
        final TeacherEntity teacherEntityMock = new TeacherEntity();
        final TeacherDetailEntity teacherDetailEntityMock = new TeacherDetailEntity();
        ReflectionUtils.setField(teacherEntityMock, "id", Long.valueOf(2L));
        teacherEntityMock.setFirstname("firstname");
        teacherEntityMock.setLastname("lastname");
        teacherEntityMock.setTeacherDetail(teacherDetailEntityMock);
        ReflectionUtils.setField(teacherDetailEntityMock, "id", Long.valueOf(3L));

        when(teacherDao.getTeacherDetailEntityById(eq(3L))).thenReturn(teacherEntityMock.getTeacherDetail());

        final TeacherDetail result = testee.saveTeacherDetail(teacher.getDetail());

        assertEquals(teacherDetail.getAddress(), result.getAddress());
        assertEquals(teacherDetail.getEmail(), result.getEmail());
        assertEquals(teacherDetail.getPhone(), result.getPhone());
    }

    @Test
    public void testSaveTeacherDetailNonExisting() {
        final TeacherDetail nonExistingTeacherDetail = new TeacherDetail("3", "address2", "phone2", "email2");
        assertNull(testee.saveTeacherDetail(nonExistingTeacherDetail));
    }

    private TeacherEntity createTeacherEntity(final Long id, final String firstname, final String lastname) {
        final TeacherEntity teacherEntity = new TeacherEntity();
        ReflectionUtils.setField(teacherEntity, "id", id);
        teacherEntity.setFirstname(firstname);
        teacherEntity.setLastname(lastname);

        final TeacherDetailEntity teacherDetailEntity = new TeacherDetailEntity();
        ReflectionUtils.setField(teacherDetailEntity, "id", id);
        teacherEntity.setTeacherDetail(teacherDetailEntity);

        return teacherEntity;
    }
}
