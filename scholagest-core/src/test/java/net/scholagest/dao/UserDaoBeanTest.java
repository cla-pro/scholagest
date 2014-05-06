package net.scholagest.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.db.entity.UserEntity;
import net.scholagest.test.util.TransactionalHelper;
import net.scholagest.utils.AbstractGuiceContextTest;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link UserDaoBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class UserDaoBeanTest extends AbstractGuiceContextTest {
    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    public UserDaoBeanTest() {
        super(true);
    }

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(UserDaoLocal.class).to(UserDaoBean.class);
    }

    @Before
    public void setUp() {
        entityContextSaver.createAndPersistEntityContext(getInstance(TransactionalHelper.class));
    }

    @Test
    public void testGetUserEntityByUsername() {
        final UserEntity persisted = entityContextSaver.getUserEntity();

        final UserDaoLocal testee = getInstance(UserDaoLocal.class);
        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final UserEntity userEntity = testee.getUserEntityByUsername(persisted.getUsername());
                assertEquals(persisted.getId(), userEntity.getId());
                assertEquals(persisted.getUsername(), userEntity.getUsername());
                assertEquals(persisted.getPassword(), userEntity.getPassword());
                assertEquals(persisted.getRole(), userEntity.getRole());
                assertNotNull(userEntity.getTeacher());

                assertNull(testee.getUserEntityByUsername("wrong"));
            }
        });
    }

    @Test
    public void testGetUserEntityById() {
        final UserEntity persisted = entityContextSaver.getUserEntity();

        final UserDaoLocal testee = getInstance(UserDaoLocal.class);
        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final UserEntity userEntity = testee.getUserEntityById(persisted.getId());
                assertEquals(persisted.getId(), userEntity.getId());
                assertEquals(persisted.getUsername(), userEntity.getUsername());
                assertEquals(persisted.getPassword(), userEntity.getPassword());
                assertEquals(persisted.getRole(), userEntity.getRole());
                assertNotNull(userEntity.getTeacher());

                assertNull(testee.getUserEntityById(-1L));
            }
        });
    }

    @Test
    public void testPersistUserEntity() {
        final TeacherEntity teacherEntity = entityContextSaver.getTeacherEntity();

        final UserEntity userEntity = new UserEntity();
        userEntity.setUsername("name");
        userEntity.setPassword("password");
        userEntity.setRole("admin");
        userEntity.setTeacher(teacherEntity);

        final UserDaoLocal testee = getInstance(UserDaoLocal.class);

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final UserEntity persistedUserEntity = testee.persistUserEntity(userEntity);
                assertNotNull(persistedUserEntity.getId());
            }
        });

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                assertNotNull(testee.getUserEntityById(userEntity.getId()));
            }
        });
    }
}
