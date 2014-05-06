package net.scholagest.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.test.util.TransactionalHelper;
import net.scholagest.utils.AbstractGuiceContextTest;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BranchPeriodDaoBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class BranchPeriodDaoBeanTest extends AbstractGuiceContextTest {
    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    public BranchPeriodDaoBeanTest() {
        super(true);
    }

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(BranchPeriodDaoLocal.class).to(BranchPeriodDaoBean.class);
    }

    @Before
    public void setUp() {
        entityContextSaver.createAndPersistEntityContext(getInstance(TransactionalHelper.class));
    }

    @Test
    public void testGetBranchPeriodEntityById() {
        final BranchPeriodEntity persisted = entityContextSaver.getBranchPeriodEntity();

        final BranchPeriodDaoLocal testee = getInstance(BranchPeriodDaoLocal.class);
        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final BranchPeriodEntity branchPeriodEntity = testee.getBranchPeriodEntityById(persisted.getId());
                assertEquals(persisted.getId(), branchPeriodEntity.getId());

                assertNull(testee.getBranchPeriodEntityById(-1L));
            }
        });
    }

    @Test
    public void testPersistBranchPeriodEntity() {
        final BranchPeriodEntity branchPeriodEntity = new BranchPeriodEntity();
        branchPeriodEntity.setBranch(entityContextSaver.getBranchEntity());
        branchPeriodEntity.setPeriod(entityContextSaver.getPeriodEntity());

        final BranchPeriodDaoLocal testee = getInstance(BranchPeriodDaoLocal.class);

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final BranchPeriodEntity persistedBranchPeriodEntity = testee.persistBranchPeriodEntity(branchPeriodEntity);
                assertNotNull(persistedBranchPeriodEntity.getId());
            }
        });

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                assertNotNull(testee.getBranchPeriodEntityById(branchPeriodEntity.getId()));
            }
        });
    }
}
