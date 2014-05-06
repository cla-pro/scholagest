package net.scholagest.tester.utils.matcher;

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
        if (item instanceof String) {
            final String toMatch = (String) item;
            final Pattern compiledRegex = Pattern.compile(regex);
            return compiledRegex.matcher(toMatch).matches();
        } else {
            return false;
        }
    }

    @Override
    public void describeTo(final Description description) {}
}
