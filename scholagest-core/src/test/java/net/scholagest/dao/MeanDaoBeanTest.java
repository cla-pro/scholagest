package net.scholagest.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import net.scholagest.db.entity.MeanEntity;
import net.scholagest.test.util.TransactionalHelper;
import net.scholagest.utils.AbstractGuiceContextTest;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link MeanDaoBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class MeanDaoBeanTest extends AbstractGuiceContextTest {
    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    public MeanDaoBeanTest() {
        super(true);
    }

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(MeanDaoLocal.class).to(MeanDaoBean.class);
    }

    @Before
    public void setUp() {
        entityContextSaver.createAndPersistEntityContext(getInstance(TransactionalHelper.class));
    }

    @Test
    public void testGetMeanEntityById() {
        final MeanEntity persisted = entityContextSaver.getMeanEntity();

        final MeanDaoLocal testee = getInstance(MeanDaoLocal.class);
        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final MeanEntity meanEntity = testee.getMeanEntityById(persisted.getId());
                assertEquals(persisted.getId(), meanEntity.getId());

                assertNull(testee.getMeanEntityById(-1L));
            }
        });
    }

    @Test
    public void testPersistMeanEntity() {
        final MeanEntity meanEntity = new MeanEntity();
        meanEntity.setGrade(null);
        meanEntity.setStudentResult(entityContextSaver.getStudentResultEntity());

        final MeanDaoLocal testee = getInstance(MeanDaoLocal.class);

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final MeanEntity persistedMeanEntity = testee.persistMeanEntity(meanEntity);
                assertNotNull(persistedMeanEntity.getId());
            }
        });

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                assertNotNull(testee.getMeanEntityById(meanEntity.getId()));
            }
        });
    }
}
