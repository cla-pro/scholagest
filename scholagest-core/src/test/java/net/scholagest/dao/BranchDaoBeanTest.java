package net.scholagest.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import net.scholagest.db.entity.BranchEntity;
import net.scholagest.test.util.TransactionalHelper;
import net.scholagest.utils.AbstractGuiceContextTest;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BranchDaoBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class BranchDaoBeanTest extends AbstractGuiceContextTest {
    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    public BranchDaoBeanTest() {
        super(true);
    }

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(BranchDaoLocal.class).to(BranchDaoBean.class);
    }

    @Before
    public void setUp() {
        entityContextSaver.createAndPersistEntityContext(getInstance(TransactionalHelper.class));
    }

    @Test
    public void testGetBranchEntityById() {
        final BranchEntity persisted = entityContextSaver.getBranchEntity();

        final BranchDaoLocal testee = getInstance(BranchDaoLocal.class);
        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final BranchEntity branchEntity = testee.getBranchEntityById(persisted.getId());
                assertEquals(persisted.getId(), branchEntity.getId());

                assertNull(testee.getBranchEntityById(-1L));
            }
        });
    }

    @Test
    public void testPersistBranchEntity() {
        final BranchEntity branchEntity = new BranchEntity();
        branchEntity.setName("branch1");
        branchEntity.setClazz(entityContextSaver.getClazzEntity());
        branchEntity.setNumerical(false);

        final BranchDaoLocal testee = getInstance(BranchDaoLocal.class);

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final BranchEntity persistedBranchEntity = testee.persistBranchEntity(branchEntity);
                assertNotNull(persistedBranchEntity.getId());
            }
        });

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                assertNotNull(testee.getBranchEntityById(branchEntity.getId()));
            }
        });
    }
}
