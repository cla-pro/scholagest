package net.scholagest.app.rest.ws.objects;

/**
 * Abstract class for all json objects
 * 
 * @author CLA
 * @since 0.13.0
 */
public abstract class BaseJson {
    private String id;

    public BaseJson() {}

    public BaseJson(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}
