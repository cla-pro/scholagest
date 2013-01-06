package net.scholagest.utils;

import static org.junit.Assert.assertEquals;

import java.util.Map;

public abstract class AbstractTest {
    public void assertMapEquals(Map<?, ?> mock, Map<?, ?> testee) {
        assertEquals(mock.size(), testee.size());

        for (Object key : mock.keySet()) {
            assertEquals(mock.get(key), testee.get(key));
        }
    }
}
