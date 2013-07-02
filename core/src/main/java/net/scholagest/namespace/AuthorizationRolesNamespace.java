package net.scholagest.namespace;

import java.util.Arrays;
import java.util.List;

public class AuthorizationRolesNamespace {
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_TEACHER = "teacher";
    public static final String ROLE_HELP_TEACHER = "helpteacher";

    public static List<String> getAdminRole() {
        return Arrays.asList(ROLE_ADMIN);
    }

    public static List<String> getAllRoles() {
        return Arrays.asList(ROLE_ADMIN, ROLE_TEACHER, ROLE_HELP_TEACHER);
    }
}
