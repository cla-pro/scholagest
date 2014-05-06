package net.scholagest.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import net.scholagest.db.entity.ResultEntity;
import net.scholagest.test.util.TransactionalHelper;
import net.scholagest.utils.AbstractGuiceContextTest;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link ResultDaoBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class ResultDaoBeanTest extends AbstractGuiceContextTest {
    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    public ResultDaoBeanTest() {
        super(true);
    }

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(ResultDaoLocal.class).to(ResultDaoBean.class);
    }

    @Before
    public void setUp() {
        entityContextSaver.createAndPersistEntityContext(getInstance(TransactionalHelper.class));
    }

    @Test
    public void testGetResultEntityById() {
        final ResultEntity persisted = entityContextSaver.getResultEntity();

        final ResultDaoLocal testee = getInstance(ResultDaoLocal.class);
        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final ResultEntity resultEntity = testee.getResultEntityById(persisted.getId());
                assertEquals(persisted.getId(), resultEntity.getId());

                assertNull(testee.getResultEntityById(-1L));
            }
        });
    }

    @Test
    public void testPersistResultEntity() {
        final ResultEntity resultEntity = new ResultEntity();
        resultEntity.setExam(entityContextSaver.getExamEntity());
        resultEntity.setGrade(null);
        resultEntity.setStudentResult(entityContextSaver.getStudentResultEntity());

        final ResultDaoLocal testee = getInstance(ResultDaoLocal.class);

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final ResultEntity persistedResultEntity = testee.persistResultEntity(resultEntity);
                assertNotNull(persistedResultEntity.getId());
            }
        });

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                assertNotNull(testee.getResultEntityById(resultEntity.getId()));
            }
        });
    }
}
