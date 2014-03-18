package net.scholagest.object;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Transfer object that represent a year.
 * 
 * @author CLA
 * @since 0.14.0
 */
public class Year extends Base {
    private String name;
    private boolean running;
    private List<String> classes;

    public Year() {
        this.classes = new ArrayList<>();
    }

    public Year(final Year toCopy) {
        super(toCopy.getId());
        this.name = toCopy.name;
        this.running = toCopy.running;
        this.classes = new ArrayList<String>(toCopy.classes);
    }

    public Year(final String id, final String name, final boolean running, final List<String> classes) {
        super(id);
        this.name = name;
        this.running = running;
        if (classes == null) {
            this.classes = new ArrayList<String>();
        } else {
            this.classes = new ArrayList<String>(classes);
        }
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object that) {
        if (that == null) {
            return false;
        } else if (!(that instanceof Year)) {
            return false;
        }

        final Year other = (Year) that;
        return new EqualsBuilder().append(getId(), other.getId()).append(name, other.name).isEquals();
    }
}
