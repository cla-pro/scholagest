package net.scholagest.services.kdom;

import java.util.Set;

public class KSet {
    private String key;
    private Set<Object> values;
    // For Gson convertion.
    @SuppressWarnings("unused")
    private boolean isHtmlList = true;

    public KSet(String key, Set<Object> values) {
        this.key = key;
        this.values = values;
    }

    public String getKey() {
        return key;
    }

    public Set<Object> getValues() {
        return values;
    }

    @Override
    public boolean equals(Object that) {
        if (that == null || !(that instanceof KSet)) {
            return false;
        }
        if (that == this) {
            return true;
        }

        KSet kSet = (KSet) that;
        return key.equals(kSet.key);
    }
}
