package net.scholagest.database.commiter;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.exceptions.HectorException;

import org.junit.Before;
import org.junit.Test;

public class InsertDBActionTest {
    @SuppressWarnings("unchecked")
    private final ColumnFamilyTemplate<String, String> template = mock(ColumnFamilyTemplate.class);
    @SuppressWarnings("unchecked")
    private final ColumnFamilyUpdater<String, String> updater = mock(ColumnFamilyUpdater.class);
    private final String key = "key";
    private final String column = "column";
    private final String value = "value";
    private final String originalValue = "originalValue";

    @Before
    public void setup() {
        when(template.createUpdater(anyString())).thenReturn(updater);
    }

    @Test
    public void testCommit() {
        InsertDBAction testee = new InsertDBAction(template, key, column, value, originalValue);

        testee.commit();

        verify(template).createUpdater(eq(key));
        verify(updater).setString(eq(column), eq(value));
        verify(template, never()).deleteColumn(eq(key), eq(column));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = HectorException.class)
    public void testCommitWithException() {
        doThrow(new HectorException("")).when(template).update(any(ColumnFamilyUpdater.class));
        InsertDBAction testee = new InsertDBAction(template, key, column, value, originalValue);

        testee.commit();
        fail("Exception expected");
    }

    @Test
    public void testRollbackWithOriginalValue() {
        InsertDBAction testee = new InsertDBAction(template, key, column, value, originalValue);

        testee.rollback();

        verify(template).createUpdater(eq(key));
        verify(updater).setString(eq(column), eq(originalValue));
        verify(template, never()).deleteColumn(eq(key), eq(column));
    }

    @Test
    public void testRollbackWithoutOriginalValue() {
        InsertDBAction testee = new InsertDBAction(template, key, column, value, null);

        testee.rollback();

        verify(template).deleteColumn(eq(key), eq(column));
        verify(template, never()).createUpdater(eq(key));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = HectorException.class)
    public void testRollbackWithException() {
        doThrow(new HectorException("")).when(template).update(any(ColumnFamilyUpdater.class));
        InsertDBAction testee = new InsertDBAction(template, key, column, value, originalValue);

        testee.rollback();
        fail("Exception expected");
    }
}
