package net.scholagest.tester.utils.matcher;

public class UUIDMatcher extends RegExMatcher {
    public UUIDMatcher() {
        super("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }
}
