package net.scholagest.tester;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Pair that represent an url parameter key=value
 * 
 * @author CLA
 * @since 0.15.0
 */
public class UrlParameter {
    private final String key;
    private final String value;

    public UrlParameter(final String key, final String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getEncodedValue() throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "UTF-8");
    }
}
