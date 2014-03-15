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

    public SessionInfo(final String token, final Subject subject) {
        this.token = token;
        this.subject = subject;
    }

    public String getToken() {
        return token;
    }

    public Subject getSubject() {
        return subject;
    }
}
