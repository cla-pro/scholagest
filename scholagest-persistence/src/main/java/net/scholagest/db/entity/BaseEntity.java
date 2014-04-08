package net.scholagest.db.entity;

public abstract class BaseEntity {
    private String id;

    public BaseEntity() {}

    public BaseEntity(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}
