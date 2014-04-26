package net.scholagest.tester.utils;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matcher;
import org.junit.Assert;

import com.google.gson.Gson;

/**
 * Class representing a json object to be compared with a response.
 * 
 * @author CLA
 * @since 0.16.0
 */
public class JsonObject {
    private final Map<String, Object> content;

    /**
     * Construct a new JsonObject with the content extracted from the parameters.
     * The parameters are given as a list of keys and values alternatively.
     * 
     * @param jsonElementKeyValues The json content
     */
    public JsonObject(final Object... jsonElementKeyValues) {
        this.content = new HashMap<>();

        for (int i = 0; i < jsonElementKeyValues.length; i += 2) {
            final Object key = jsonElementKeyValues[i];
            final Object value = jsonElementKeyValues[i + 1];

            content.put((String) key, value);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void assertEquals(final String json) {
        final Map<String, Object> toCompare = new Gson().fromJson(json, Map.class);

        for (final String key : content.keySet()) {
            assertTrue(toCompare.containsKey(key));

            final Object expected = content.get(key);
            final Object given = toCompare.get(key);

            if (expected instanceof Matcher) {
                final Matcher matcher = (Matcher) expected;
                matcher.matches(given);
            } else if (expected instanceof JsonObject) {
                final JsonObject expectedJson = (JsonObject) expected;
                expectedJson.assertEquals((String) given);
            } else {
                Assert.assertEquals(expected, given);
            }
        }
    }
}
