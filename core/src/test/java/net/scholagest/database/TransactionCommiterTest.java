package net.scholagest.database;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import me.prettyprint.hector.api.exceptions.HectorException;
import net.scholagest.database.commiter.DBAction;

import org.junit.Test;
import org.mockito.Mockito;

public class TransactionCommiterTest {
    @Test
        public void testAddActionActionAndCommit() {
            DBAction dbAction1 = mock(DBAction.class);
            DBAction dbAction2 = mock(DBAction.class);
    
            TransactionCommiter testee = new TransactionCommiter();
    
            testee.addAction(dbAction1);
            testee.addAction(dbAction2);
    
            testee.commit();
    
            verify(dbAction1).commit();
            verify(dbAction2).commit();
            verify(dbAction1, never()).rollback();
            verify(dbAction2, never()).rollback();
        }

    @Test(expected = HectorException.class)
        public void testAddActionActionAndRollbackAtFirstAction() {
            DBAction dbAction1 = mock(DBAction.class);
            Mockito.doThrow(new HectorException("")).when(dbAction1).commit();
            DBAction dbAction2 = mock(DBAction.class);
    
            TransactionCommiter testee = new TransactionCommiter();
    
            testee.addAction(dbAction1);
            testee.addAction(dbAction2);
    
            testee.commit();
    
            verify(dbAction1).commit();
            verify(dbAction2, never()).commit();
            verify(dbAction1).rollback();
            verify(dbAction2, never()).rollback();
        }

    @Test(expected = HectorException.class)
        public void testAddActionActionAndRollbackAll() {
            DBAction dbAction1 = mock(DBAction.class);
            DBAction dbAction2 = mock(DBAction.class);
            Mockito.doThrow(new HectorException("")).when(dbAction2).commit();
    
            TransactionCommiter testee = new TransactionCommiter();
    
            testee.addAction(dbAction1);
            testee.addAction(dbAction2);
    
            testee.commit();
    
            verify(dbAction1).commit();
            verify(dbAction2).commit();
            verify(dbAction1).rollback();
            verify(dbAction2).rollback();
        }

    @Test
        public void testAddActionActionAndRollbackWithoutCommit() {
            DBAction dbAction1 = mock(DBAction.class);
            DBAction dbAction2 = mock(DBAction.class);
    
            TransactionCommiter testee = new TransactionCommiter();
    
            testee.addAction(dbAction1);
            testee.addAction(dbAction2);
    
            testee.rollback();
    
            verify(dbAction1, never()).commit();
            verify(dbAction2, never()).commit();
            verify(dbAction1, never()).rollback();
            verify(dbAction2, never()).rollback();
        }
}
