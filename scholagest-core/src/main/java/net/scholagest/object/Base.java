package net.scholagest.object;

/**
 * Abstract class for all transfer objects
 * 
 * @author CLA
 * @since 0.13.0
 */
public abstract class Base {
    private String id;

    public Base() {}

    public Base(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}
