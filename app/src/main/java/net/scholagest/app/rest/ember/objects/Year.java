package net.scholagest.app.rest.ember.objects;

import java.util.ArrayList;
import java.util.List;

public class Year {
    private String id;
    private String name;
    private boolean running;
    private List<String> classes;

    public Year() {
        this.classes = new ArrayList<>();
    }

    public Year(final String id, final String name, final boolean running, final List<String> classes) {
        this.id = id;
        this.name = name;
        this.running = running;
        this.classes = classes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }
}
