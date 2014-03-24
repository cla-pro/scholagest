package net.scholagest.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for {@link Result}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class ResultTest {
    @Test
    public void testConstructorCopy() {
        final Result toCopy = new Result("id", 3.5, "exam", "studentResult");
        final Result copied = new Result(toCopy);

        assertEquals(toCopy.getId(), copied.getId());
        assertEquals(toCopy.getGrade(), copied.getGrade());
        assertEquals(toCopy.getExam(), copied.getExam());
        assertEquals(toCopy.getStudentResult(), copied.getStudentResult());
    }

    @Test
    public void testEquals() {
        final Result result1 = new Result(null, null, null, null);
        final Result result2 = new Result(null, 3.5, "exam1", "studentResutl1");
        final Result result3 = new Result("1", 4.5, "exam2", "studentResult2");
        final Result result4 = new Result("1", 3.5, "exam1", "studentResult1");
        final Result result5 = new Result("1", 3.5, "exam1", "studentResult1");

        assertFalse(result1.equals(null));
        assertFalse(result1.equals(new Object()));
        assertFalse(result1.equals(result2));
        assertFalse(result1.equals(result3));
        assertFalse(result1.equals(result4));
        assertFalse(result1.equals(result5));
        assertFalse(result2.equals(result3));
        assertFalse(result2.equals(result4));
        assertFalse(result2.equals(result5));
        assertFalse(result3.equals(result4));
        assertFalse(result3.equals(result5));
        assertTrue(result4.equals(result5));
    }
}
