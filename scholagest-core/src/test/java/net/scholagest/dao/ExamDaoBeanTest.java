package net.scholagest.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import net.scholagest.db.entity.ExamEntity;
import net.scholagest.test.util.TransactionalHelper;
import net.scholagest.utils.AbstractGuiceContextTest;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link ExamDaoBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class ExamDaoBeanTest extends AbstractGuiceContextTest {
    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    public ExamDaoBeanTest() {
        super(true);
    }

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(ExamDaoLocal.class).to(ExamDaoBean.class);
    }

    @Before
    public void setUp() {
        entityContextSaver.createAndPersistEntityContext(getInstance(TransactionalHelper.class));
    }

    @Test
    public void testGetExamEntityById() {
        final ExamEntity persisted = entityContextSaver.getExamEntity();

        final ExamDaoLocal testee = getInstance(ExamDaoLocal.class);
        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final ExamEntity examEntity = testee.getExamEntityById(persisted.getId());
                assertEquals(persisted.getId(), examEntity.getId());

                assertNull(testee.getExamEntityById(-1L));
            }
        });
    }

    @Test
    public void testPersistExamEntity() {
        final ExamEntity examEntity = new ExamEntity();
        examEntity.setName("exam1");
        examEntity.setBranchPeriod(entityContextSaver.getBranchPeriodEntity());
        examEntity.setCoeff(2);

        final ExamDaoLocal testee = getInstance(ExamDaoLocal.class);

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final ExamEntity persistedExamEntity = testee.persistExamEntity(examEntity);
                assertNotNull(persistedExamEntity.getId());
            }
        });

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                assertNotNull(testee.getExamEntityById(examEntity.getId()));
            }
        });
    }
}
