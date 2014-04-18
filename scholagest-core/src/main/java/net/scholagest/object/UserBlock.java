package net.scholagest.object;

import java.util.List;

/**
 * Used to wrap the user as well as the related teacher and his classes.
 * 
 * @author CLA
 * @since 0.16.0
 */
public class UserBlock {
    private User user;
    private Teacher teacher;
    private List<Clazz> clazzes;

    public UserBlock(final User user, final Teacher teacher, final List<Clazz> clazzes) {
        this.user = user;
        this.teacher = teacher;
        this.clazzes = clazzes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(final Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Clazz> getClazzes() {
        return clazzes;
    }

    public void setClazzes(final List<Clazz> clazzes) {
        this.clazzes = clazzes;
    }
}
