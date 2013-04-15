package net.scholagest.tester;

import java.util.HashMap;
import java.util.Map;

public class Placeholder {
    private static final String PLACEHOLDER_DELIMITER = "$$";

    private Map<String, String> placeholders = new HashMap<>();

    public String replacePlaceholdersInString(String toCheck) {
        int delimiterLength = PLACEHOLDER_DELIMITER.length();

        int startIndex = toCheck.indexOf(PLACEHOLDER_DELIMITER);
        while (startIndex != -1) {
            int endIndex = toCheck.indexOf(PLACEHOLDER_DELIMITER, startIndex + delimiterLength);

            if (endIndex != -1) {
                String before = toCheck.substring(0, startIndex);
                String toReplace = toCheck.substring(startIndex + delimiterLength, endIndex);
                String after = "";

                int afterStartIndex = endIndex + delimiterLength;
                if (afterStartIndex >= toCheck.length()) {
                    after = toCheck.substring(endIndex + delimiterLength);
                }

                toCheck = before + placeholders.get(toReplace) + after;
            } else {
                System.err.println("Unended placeholder");
                break;
            }

            startIndex = toCheck.indexOf(PLACEHOLDER_DELIMITER, endIndex + delimiterLength);
        }

        return toCheck;
    }

    public void storeValue(String name, String value) {
        placeholders.put(name, value);
    }
}
