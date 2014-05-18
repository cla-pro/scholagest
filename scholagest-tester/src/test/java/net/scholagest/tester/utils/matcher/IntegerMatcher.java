package net.scholagest.tester.utils.matcher;

public class IntegerMatcher extends DoubleMatcher {
    public IntegerMatcher(final int expected) {
        super(Integer.valueOf(expected).doubleValue());
    }
}
