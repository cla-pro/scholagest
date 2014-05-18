package net.scholagest.tester.utils.matcher;

import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class RegExMatcher extends BaseMatcher<String> {
    private final String regex;

    public RegExMatcher(final String regex) {
        this.regex = regex;
    }

    @Override
    public boolean matches(final Object item) {
        final Pattern compiledRegex = Pattern.compile(regex);
        assertTrue(compiledRegex.matcher(item.toString()).matches());
        return true;
    }

    @Override
    public void describeTo(final Description description) {}
}
