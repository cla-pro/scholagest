package net.scholagest.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import net.scholagest.db.entity.StudentEntity;
import net.scholagest.db.entity.StudentMedicalEntity;
import net.scholagest.db.entity.StudentPersonalEntity;
import net.scholagest.test.util.TransactionalHelper;
import net.scholagest.utils.AbstractGuiceContextTest;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link StudentDaoBean}
 * 
 * @author CLA
 * @since 0.16.0
 */
public class StudentDaoBeanTest extends AbstractGuiceContextTest {
    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    public StudentDaoBeanTest() {
        super(true);
    }

    @Override
    protected void configureContext(final TestGuiceContext module) {
        module.bind(StudentDaoLocal.class).to(StudentDaoBean.class);
    }

    @Before
    public void setUp() {
        entityContextSaver.createAndPersistEntityContext(getInstance(TransactionalHelper.class));
    }

    @Test
    public void testGetAllStudentEntity() {
        final StudentDaoLocal testee = getInstance(StudentDaoLocal.class);

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                assertEquals(1, testee.getAllStudentEntity().size());
            }
        });
    }

    @Test
    public void testGetStudentEntityById() {
        final StudentEntity persisted = entityContextSaver.getStudentEntity();

        final StudentDaoLocal testee = getInstance(StudentDaoLocal.class);
        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final StudentEntity studentEntity = testee.getStudentEntityById(persisted.getId());
                assertEquals(persisted.getId(), studentEntity.getId());

                assertNull(testee.getStudentEntityById(-1L));
            }
        });
    }

    @Test
    public void testGetStudentPersonalEntityById() {
        final StudentPersonalEntity persisted = entityContextSaver.getStudentPersonalEntity();

        final StudentDaoLocal testee = getInstance(StudentDaoLocal.class);
        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final StudentPersonalEntity studentPersonalEntity = testee.getStudentPersonalEntityById(persisted.getId());
                assertEquals(persisted.getId(), studentPersonalEntity.getId());

                assertNull(testee.getStudentPersonalEntityById(-1L));
            }
        });
    }

    @Test
    public void testGetStudentMedicalEntityById() {
        final StudentMedicalEntity persisted = entityContextSaver.getStudentMedicalEntity();

        final StudentDaoLocal testee = getInstance(StudentDaoLocal.class);
        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final StudentMedicalEntity studentMedicalEntity = testee.getStudentMedicalEntityById(persisted.getId());
                assertEquals(persisted.getId(), studentMedicalEntity.getId());

                assertNull(testee.getStudentMedicalEntityById(-1L));
            }
        });
    }

    @Test
    public void testPersistStudentEntity() {
        final StudentPersonalEntity studentPersonalEntity = new StudentPersonalEntity();
        final StudentMedicalEntity studentMedicalEntity = new StudentMedicalEntity();

        final StudentEntity studentEntity = new StudentEntity();
        studentEntity.setFirstname("firstname");
        studentEntity.setLastname("lastname");
        studentEntity.setStudentMedical(studentMedicalEntity);
        studentEntity.setStudentPersonal(studentPersonalEntity);

        final StudentDaoLocal testee = getInstance(StudentDaoLocal.class);

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                final StudentEntity persistedStudentEntity = testee.persistStudentEntity(studentEntity);
                assertNotNull(persistedStudentEntity.getId());
                assertNotNull(persistedStudentEntity.getStudentMedical().getId());
                assertNotNull(persistedStudentEntity.getStudentPersonal().getId());
            }
        });

        executeInTransaction(new Runnable() {
            @Override
            public void run() {
                assertNotNull(testee.getStudentEntityById(studentEntity.getId()));
                assertNotNull(testee.getStudentPersonalEntityById(studentPersonalEntity.getId()));
                assertNotNull(testee.getStudentMedicalEntityById(studentMedicalEntity.getId()));
            }
        });
    }
}
