package net.scholagest.app.rest.object;

import java.util.Map;

public class RestYearRenameRequest {
    private String token;
    private Map<String, String> object;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setObject(Map<String, String> object) {
        this.object = object;
    }

    public String getKey() {
        return object.get("key");
    }

    public String getNewYearName() {
        return object.get("newYearName");
    }
}
