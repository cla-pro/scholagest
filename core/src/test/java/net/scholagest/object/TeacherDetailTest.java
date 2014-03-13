package net.scholagest.object;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for {@see TeacherDetail}
 * 
 * @author CLA
 * @since 0.13.0
 */
public class TeacherDetailTest {
    @Test
    public void testEquals() {
        final TeacherDetail teacherDetail1 = new TeacherDetail(null, null, null, null);
        final TeacherDetail teacherDetail2 = new TeacherDetail(null, "address", "email", "phone");
        final TeacherDetail teacherDetail3 = new TeacherDetail(null, "otherAddress", "otherEmail", "otherPhone");
        final TeacherDetail teacherDetail4 = new TeacherDetail("1", "address", "email", "phone");
        final TeacherDetail teacherDetail5 = new TeacherDetail("1", "address", "email", "phone");

        assertFalse(teacherDetail1.equals(teacherDetail2));
        assertFalse(teacherDetail1.equals(teacherDetail3));
        assertFalse(teacherDetail1.equals(teacherDetail4));
        assertFalse(teacherDetail1.equals(teacherDetail5));
        assertFalse(teacherDetail2.equals(teacherDetail3));
        assertFalse(teacherDetail2.equals(teacherDetail4));
        assertFalse(teacherDetail2.equals(teacherDetail5));
        assertFalse(teacherDetail3.equals(teacherDetail4));
        assertFalse(teacherDetail3.equals(teacherDetail5));
        assertTrue(teacherDetail4.equals(teacherDetail5));
    }
}
