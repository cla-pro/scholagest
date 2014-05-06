package net.scholagest.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import net.scholagest.db.entity.StudentResultEntity;
import net.scholagest.test.util.TransactionalHelper;
import net.scholagest.utils.AbstractGuiceContextTest;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link StudentResultDaoBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class StudentResultDaoBeanTest extends AbstractGuiceContextTest {
    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    public StudentResultDaoBeanTest() {
        super(true);
    }

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(StudentResultDaoLocal.class).to(StudentResultDaoBean.class);
    }

    @Before
    public void setUp() {
        entityContextSaver.createAndPersistEntityContext(getInstance(TransactionalHelper.class));
    }

    @Test
    public void testGetStudentResultEntityById() {
        final StudentResultEntity persisted = entityContextSaver.getStudentResultEntity();

        final StudentResultDaoLocal testee = getInstance(StudentResultDaoLocal.class);
        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final StudentResultEntity studentResultEntity = testee.getStudentResultEntityById(persisted.getId());
                assertEquals(persisted.getId(), studentResultEntity.getId());

                assertNull(testee.getStudentResultEntityById(-1L));
            }
        });
    }

    @Test
    public void testPersistStudentResultEntity() {
        final StudentResultEntity studentResultEntity = new StudentResultEntity();
        studentResultEntity.setActive(false);
        studentResultEntity.setBranchPeriod(entityContextSaver.getBranchPeriodEntity());
        studentResultEntity.setStudent(entityContextSaver.getStudentEntity());

        final StudentResultDaoLocal testee = getInstance(StudentResultDaoLocal.class);

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final StudentResultEntity persistedStudentResultEntity = testee.persistStudentResultEntity(studentResultEntity);
                assertNotNull(persistedStudentResultEntity.getId());
            }
        });

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                assertNotNull(testee.getStudentResultEntityById(studentResultEntity.getId()));
            }
        });
    }
}
