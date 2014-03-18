package net.scholagest.app.rest.ws.objects;

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
