package net.scholagest.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import net.scholagest.db.entity.ClazzEntity;
import net.scholagest.test.util.TransactionalHelper;
import net.scholagest.utils.AbstractGuiceContextTest;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link ClazzDaoBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class ClazzDaoBeanTest extends AbstractGuiceContextTest {
    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    public ClazzDaoBeanTest() {
        super(true);
    }

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(ClazzDaoLocal.class).to(ClazzDaoBean.class);
    }

    @Before
    public void setUp() {
        entityContextSaver.createAndPersistEntityContext(getInstance(TransactionalHelper.class));
    }

    @Test
    public void testGetClazzEntityById() {
        final ClazzEntity persisted = entityContextSaver.getClazzEntity();

        final ClazzDaoLocal testee = getInstance(ClazzDaoLocal.class);
        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final ClazzEntity clazzEntity = testee.getClazzEntityById(persisted.getId());
                assertEquals(persisted.getId(), clazzEntity.getId());

                assertNull(testee.getClazzEntityById(-1L));
            }
        });
    }

    @Test
    public void testPersistClazzEntity() {
        final ClazzEntity clazzEntity = new ClazzEntity();
        clazzEntity.setName("clazz1");
        clazzEntity.setYear(entityContextSaver.getYearEntity());

        final ClazzDaoLocal testee = getInstance(ClazzDaoLocal.class);

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final ClazzEntity persistedClazzEntity = testee.persistClazzEntity(clazzEntity);
                assertNotNull(persistedClazzEntity.getId());
            }
        });

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                assertNotNull(testee.getClazzEntityById(clazzEntity.getId()));
            }
        });
    }
}
