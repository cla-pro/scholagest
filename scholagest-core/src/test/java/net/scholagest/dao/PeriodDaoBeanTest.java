package net.scholagest.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import net.scholagest.db.entity.PeriodEntity;
import net.scholagest.test.util.TransactionalHelper;
import net.scholagest.utils.AbstractGuiceContextTest;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link PeriodDaoBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class PeriodDaoBeanTest extends AbstractGuiceContextTest {
    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    public PeriodDaoBeanTest() {
        super(true);
    }

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(PeriodDaoLocal.class).to(PeriodDaoBean.class);
    }

    @Before
    public void setUp() {
        entityContextSaver.createAndPersistEntityContext(getInstance(TransactionalHelper.class));
    }

    @Test
    public void testGetPeriodEntityById() {
        final PeriodEntity persisted = entityContextSaver.getPeriodEntity();

        final PeriodDaoLocal testee = getInstance(PeriodDaoLocal.class);
        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final PeriodEntity periodEntity = testee.getPeriodEntityById(persisted.getId());
                assertEquals(persisted.getId(), periodEntity.getId());

                assertNull(testee.getPeriodEntityById(-1L));
            }
        });
    }

    @Test
    public void testPersistPeriodEntity() {
        final PeriodEntity periodEntity = new PeriodEntity();
        periodEntity.setClazz(entityContextSaver.getClazzEntity());
        periodEntity.setName("period1");

        final PeriodDaoLocal testee = getInstance(PeriodDaoLocal.class);

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final PeriodEntity persistedPeriodEntity = testee.persistPeriodEntity(periodEntity);
                assertNotNull(persistedPeriodEntity.getId());
            }
        });

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                assertNotNull(testee.getPeriodEntityById(periodEntity.getId()));
            }
        });
    }
}
