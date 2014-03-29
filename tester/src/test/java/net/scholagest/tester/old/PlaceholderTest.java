package net.scholagest.tester.old;

import static org.junit.Assert.assertEquals;
import net.scholagest.tester.old.Placeholder;

import org.junit.Test;

public class PlaceholderTest {
    @Test
    public void testReplacePlaceholdersInString() {
        String stringWithPlaceholders = "{token:$$token$$}";
        String stringNoReplacement = "{token:null}";

        Placeholder placeholder = new Placeholder();

        assertEquals(stringNoReplacement, placeholder.replacePlaceholdersInString(stringWithPlaceholders));

        placeholder.storeValue("t", "abcd");

        assertEquals(stringNoReplacement, placeholder.replacePlaceholdersInString(stringWithPlaceholders));

        placeholder.storeValue("token", "897c41d6-76c4-4a1e-a393-26f0b55f1720");

        assertEquals("{token:897c41d6-76c4-4a1e-a393-26f0b55f1720}", placeholder.replacePlaceholdersInString(stringWithPlaceholders));
    }
}
