package net.scholagest.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import net.scholagest.db.entity.YearEntity;
import net.scholagest.test.util.TransactionalHelper;
import net.scholagest.utils.AbstractGuiceContextTest;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link YearDaoBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class YearDaoBeanTest extends AbstractGuiceContextTest {
    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    public YearDaoBeanTest() {
        super(true);
    }

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(YearDaoLocal.class).to(YearDaoBean.class);
    }

    @Before
    public void setUp() {
        entityContextSaver.createAndPersistEntityContext(getInstance(TransactionalHelper.class));
    }

    @Test
    public void testGetAllYearEntity() {
        final YearDaoLocal testee = getInstance(YearDaoLocal.class);

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                assertEquals(1, testee.getAllYearEntity().size());
            }
        });
    }

    @Test
    public void testGetYearEntityById() {
        final YearEntity persisted = entityContextSaver.getYearEntity();

        final YearDaoLocal testee = getInstance(YearDaoLocal.class);
        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final YearEntity yearEntity = testee.getYearEntityById(persisted.getId());
                assertEquals(persisted.getId(), yearEntity.getId());
                assertEquals(persisted.getName(), yearEntity.getName());
                assertNotNull(yearEntity.getClasses());

                assertNull(testee.getYearEntityById(-1L));
            }
        });
    }

    @Test
    public void testPersistYearEntity() {
        final YearEntity yearEntity = new YearEntity();
        yearEntity.setName("name");

        final YearDaoLocal testee = getInstance(YearDaoLocal.class);

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final YearEntity persistedYearEntity = testee.persistYearEntity(yearEntity);
                assertNotNull(persistedYearEntity.getId());
            }
        });

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                assertNotNull(testee.getYearEntityById(yearEntity.getId()));
            }
        });
    }
}
