package net.scholagest.database.cache;

public class CacheResult {
    private boolean found;
    private Object value;

    public CacheResult(boolean found, Object value) {
        super();
        this.found = found;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }
}
