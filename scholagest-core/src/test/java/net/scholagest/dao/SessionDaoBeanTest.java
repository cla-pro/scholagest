package net.scholagest.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.UUID;

import net.scholagest.db.entity.SessionEntity;
import net.scholagest.test.util.TransactionalHelper;
import net.scholagest.utils.AbstractGuiceContextTest;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link SessionDaoBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class SessionDaoBeanTest extends AbstractGuiceContextTest {
    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    public SessionDaoBeanTest() {
        super(true);
    }

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(SessionDaoLocal.class).to(SessionDaoBean.class);
    }

    @Before
    public void setUp() {
        entityContextSaver.createAndPersistEntityContext(getInstance(TransactionalHelper.class));
    }

    @Test
    public void testGetSessionEntityById() {
        final SessionEntity persisted = entityContextSaver.getSessionEntity();

        final SessionDaoLocal testee = getInstance(SessionDaoLocal.class);
        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final SessionEntity sessionEntity = testee.getSessionEntityById(persisted.getId());
                assertEquals(persisted.getId(), sessionEntity.getId());

                assertNull(testee.getSessionEntityById("invalid"));
            }
        });
    }

    @Test
    public void testPersistSessionEntity() {
        final SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setExpirationDate(new DateTime().plusHours(2));
        sessionEntity.setId(UUID.randomUUID().toString());
        sessionEntity.setUser(entityContextSaver.getUserEntity());

        final SessionDaoLocal testee = getInstance(SessionDaoLocal.class);

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final SessionEntity persistedSessionEntity = testee.persistSessionEntity(sessionEntity);
                assertNotNull(persistedSessionEntity.getId());
            }
        });

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                assertNotNull(testee.getSessionEntityById(sessionEntity.getId()));
            }
        });
    }
}
