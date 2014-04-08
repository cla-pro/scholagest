package net.scholagest.app.rest.ws.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Json object representing a student classes
 * 
 * @author CLA
 * @since 0.14.0
 */
public class StudentClassJson extends BaseJson {
    private List<String> currentClasses;
    private List<String> oldClasses;

    public StudentClassJson() {
        this.currentClasses = new ArrayList<>();
        this.oldClasses = new ArrayList<>();
    }

    public StudentClassJson(final String id, final List<String> currentClasses, final List<String> oldClasses) {
        super(id);
        this.currentClasses = new ArrayList<>(currentClasses);
        this.oldClasses = new ArrayList<>(oldClasses);
    }

    public List<String> getCurrentClasses() {
        return currentClasses;
    }

    public void setCurrentClasses(final List<String> currentClasses) {
        this.currentClasses = currentClasses;
    }

    public List<String> getOldClasses() {
        return oldClasses;
    }

    public void setOldClasses(final List<String> oldClasses) {
        this.oldClasses = oldClasses;
    }
}
