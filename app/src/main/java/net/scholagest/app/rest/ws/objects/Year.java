package net.scholagest.app.rest.ws.objects;

import java.util.ArrayList;
import java.util.List;

public class Year extends BaseJson {
    private String name;
    private boolean running;
    private List<String> classes;

    public Year() {
        this.classes = new ArrayList<>();
    }

    public Year(final String id, final String name, final boolean running, final List<String> classes) {
        super(id);
        this.name = name;
        this.running = running;
        this.classes = new ArrayList<String>(classes);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(final boolean running) {
        this.running = running;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(final List<String> classes) {
        this.classes = classes;
    }
}
