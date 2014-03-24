package net.scholagest.app.rest.ws.objects;

import java.util.ArrayList;
import java.util.List;

public class StudentClassesJson extends BaseJson {
    private final List<String> currentClasses;
    private final List<String> oldClasses;

    public StudentClassesJson() {
        this.currentClasses = new ArrayList<>();
        this.oldClasses = new ArrayList<>();
    }

    public StudentClassesJson(final String id, final List<String> currentClasses, final List<String> oldClasses) {
        super(id);
        this.currentClasses = new ArrayList<>(currentClasses);
        this.oldClasses = new ArrayList<>(oldClasses);
    }

    public List<String> getCurrentClasses() {
        return currentClasses;
    }

    public List<String> getOldClasses() {
        return oldClasses;
    }
}
