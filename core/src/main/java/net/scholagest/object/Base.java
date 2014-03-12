package net.scholagest.object;

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
