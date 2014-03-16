package net.scholagest.app.rest.ws.converter;

import net.scholagest.app.rest.ws.objects.UserJson;
import net.scholagest.object.User;

/**
 * Method to convert from transfer object {@see User} to json {@see UserJson}
 * 
 * @author CLA
 * @since 0.13.0
 */
public class UserJsonConverter {
    /**
     * Convert a {@see User} to its json version {@see UserJson}.
     * 
     * @param user The user to convert
     * @return The converted json user
     */
    public UserJson convertToUserJson(final User user) {
        final UserJson userJson = new UserJson();

        userJson.setId(user.getId());
        userJson.setRole(user.getRole());
        userJson.setTeacher(user.getTeacherId());

        return userJson;
    }
}
