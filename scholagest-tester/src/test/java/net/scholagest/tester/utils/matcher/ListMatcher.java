package net.scholagest.tester.utils.matcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.scholagest.tester.utils.JsonObject;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import com.google.gson.Gson;

public class ListMatcher extends BaseMatcher<String> {
    private final List<Object> elements;

    public ListMatcher(final Object... elements) {
        this(Arrays.asList(elements));
    }

    public ListMatcher(final List<Object> elements) {
        this.elements = elements;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public boolean matches(final Object item) {
        final List<Object> itemAsList;
        if (item instanceof String) {
            itemAsList = parseAsList((String) item);
        } else if (item instanceof List) {
            itemAsList = new ArrayList<Object>((List) item);
        } else {
            fail("Item of type " + item.getClass().getName() + " not handled by a ListMatcher");
            return false;
        }

        matchesList(elements, itemAsList);

        return true;
    }

    private void matchesList(final List<Object> expectedList, final List<Object> receivedList) {
        assertEquals(expectedList.size(), receivedList.size());

        final Iterator<Object> expectedIterator = expectedList.iterator();
        final Iterator<Object> receivedIterator = receivedList.iterator();

        while (expectedIterator.hasNext()) {
            matchesObject(expectedIterator.next(), receivedIterator.next());
        }
    }

    @SuppressWarnings("unchecked")
    private List<Object> parseAsList(final String item) {
        return new Gson().fromJson(item, List.class);
    }

    @SuppressWarnings("rawtypes")
    private boolean matchesObject(final Object expected, final Object received) {
        try {
            if (expected instanceof Matcher) {
                final Matcher matcher = (Matcher) expected;
                return matcher.matches(received);
            } else if (expected instanceof JsonObject) {
                final JsonObject expectedJson = (JsonObject) expected;
                expectedJson.assertEquals(received.toString());
                return true;
            } else {
                assertEquals(expected, received);
                return true;
            }
        } catch (final AssertionError e) {
            return false;
        }
    }

    @Override
    public void describeTo(final Description description) {}
}
