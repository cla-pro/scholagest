package net.scholagest.object;

import org.apache.shiro.subject.Subject;

/**
 * Contains the session information required to execute the request.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class SessionInfo {
    private final String token;
    private final Subject subject;
    private final User user;

    public SessionInfo(final String token, final Subject subject, final User user) {
        this.token = token;
        this.subject = subject;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public Subject getSubject() {
        return subject;
    }

    public User getUser() {
        return user;
    }
}
