package net.scholagest.tester.utils.matcher;

import static org.junit.Assert.assertEquals;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class EqMatcher extends BaseMatcher<Object> {
    private final Object object;

    public EqMatcher(final Object object) {
        this.object = object;
    }

    @Override
    public boolean matches(final Object item) {
        assertEquals(object, item);
        return true;
    }

    @Override
    public void describeTo(final Description description) {}
}
