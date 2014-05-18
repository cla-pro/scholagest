package net.scholagest.tester.utils.matcher;

public class LongMatcher extends DoubleMatcher {
    public LongMatcher(final long expected) {
        super(Long.valueOf(expected).doubleValue());
    }
}
