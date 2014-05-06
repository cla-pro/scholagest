package net.scholagest.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import net.scholagest.db.entity.TeacherDetailEntity;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.test.util.TransactionalHelper;
import net.scholagest.utils.AbstractGuiceContextTest;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link TeacherDaoBean}
 * 
 * @author CLA
 * @since 0.15.0
 */
public class TeacherDaoBeanTest extends AbstractGuiceContextTest {
    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    public TeacherDaoBeanTest() {
        super(true);
    }

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(TeacherDaoLocal.class).to(TeacherDaoBean.class);
    }

    @Before
    public void setUp() {
        entityContextSaver.createAndPersistEntityContext(getInstance(TransactionalHelper.class));
    }

    @Test
    public void testGetAllTeacherEntity() {
        final TeacherDaoLocal testee = getInstance(TeacherDaoLocal.class);

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                assertEquals(1, testee.getAllTeacherEntity().size());
            }
        });
    }

    @Test
    public void testGetTeacherEntityById() {
        final TeacherEntity persisted = entityContextSaver.getTeacherEntity();

        final TeacherDaoLocal testee = getInstance(TeacherDaoLocal.class);
        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final TeacherEntity teacherEntity = testee.getTeacherEntityById(persisted.getId());
                assertEquals(persisted.getId(), teacherEntity.getId());
                assertEquals(persisted.getFirstname(), teacherEntity.getFirstname());
                assertEquals(persisted.getLastname(), teacherEntity.getLastname());
                assertNotNull(teacherEntity.getTeacherDetail());
                assertNotNull(teacherEntity.getClazzes());

                assertNull(testee.getTeacherEntityById(-1L));
            }
        });
    }

    @Test
    public void testGetTeacherDetailEntityById() {
        final TeacherEntity persisted = entityContextSaver.getTeacherEntity();
        final TeacherDetailEntity persistedDetail = persisted.getTeacherDetail();

        final TeacherDaoLocal testee = getInstance(TeacherDaoLocal.class);
        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final TeacherDetailEntity teacherDetailEntity = testee.getTeacherDetailEntityById(persisted.getId());
                assertEquals(persistedDetail.getId(), teacherDetailEntity.getId());
                assertEquals(persistedDetail.getAddress(), teacherDetailEntity.getAddress());
                assertEquals(persistedDetail.getEmail(), teacherDetailEntity.getEmail());
                assertEquals(persistedDetail.getPhone(), teacherDetailEntity.getPhone());
                assertEquals(persisted.getId(), teacherDetailEntity.getTeacher().getId());

                assertNull(testee.getTeacherDetailEntityById(-1L));
            }
        });
    }

    @Test
    public void testPersistTeacherEntity() {
        final TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setFirstname("firstname");
        teacherEntity.setLastname("lastname");

        final TeacherDetailEntity teacherDetailEntity = new TeacherDetailEntity();
        teacherEntity.setTeacherDetail(teacherDetailEntity);

        final TeacherDaoLocal testee = getInstance(TeacherDaoLocal.class);

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final TeacherEntity persistedTeacherEntity = testee.persistTeacherEntity(teacherEntity);
                assertNotNull(persistedTeacherEntity.getId());
                assertNotNull(persistedTeacherEntity.getTeacherDetail().getId());
            }
        });

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                assertNotNull(testee.getTeacherEntityById(teacherEntity.getId()));
            }
        });
    }
}
