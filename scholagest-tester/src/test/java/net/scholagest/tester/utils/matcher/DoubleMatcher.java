package net.scholagest.tester.utils.matcher;

import static org.junit.Assert.assertEquals;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class DoubleMatcher extends BaseMatcher<Integer> {
    private static final double EPSILON = 0.00001;

    private final double expected;

    public DoubleMatcher(final double expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(final Object item) {
        if (item instanceof Number) {
            final Number number = (Number) item;
            assertEquals(expected, number.doubleValue(), EPSILON);
        }
        return false;
    }

    @Override
    public void describeTo(final Description description) {}
}
