package net.scholagest.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.scholagest.utils.AbstractGuiceContextTest;

import org.junit.Ignore;
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

    @Ignore
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
                assertEquals(3, testee.getAllTeacherEntity().size());
            }
        });
    }
}
