package net.scholagest.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.scholagest.db.entity.TeacherDetailEntity;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.utils.AbstractGuiceContextTest;

import org.junit.Test;

/**
 * Test class for {@link TeacherDaoBean}
 * 
 * @author CLA
 * @since 0.15.0
 */
public class TeacherDaoBeanTest extends AbstractGuiceContextTest {
    public TeacherDaoBeanTest() {
        super(true);
    }

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(TeacherDaoLocal.class).to(TeacherDaoBean.class);
    }

    @Test
    public void testGetAllTeacherEntity() {
        final TeacherDaoLocal testee = getInstance(TeacherDaoLocal.class);

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                assertTrue(testee.getAllTeacherEntity().isEmpty());
            }
        });

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final TeacherDetailEntity teacherDetailEntity = new TeacherDetailEntity();
                final TeacherEntity teacherEntity = new TeacherEntity();
                teacherEntity.setFirstname("firstname");
                teacherEntity.setLastname("lastname");
                teacherEntity.setTeacherDetail(teacherDetailEntity);

                testee.persistTeacherEntity(teacherEntity);
            }
        });

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                assertEquals(1, testee.getAllTeacherEntity().size());
            }
        });
    }
}
