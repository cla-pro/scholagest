package net.scholagest.app.rest.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.scholagest.app.rest.ws.objects.BaseJson;

public class IdHelper {
    public static String getNextId(final Set<String> existingIds) {
        int max = 0;
        for (final String stringId : existingIds) {
            final Integer id = Integer.valueOf(stringId);
            if (id > max) {
                max = id;
            }
        }
        return "" + (max + 1);
    }

    public static List<String> extractIds(final List<? extends BaseJson> baseList) {
        final List<String> ids = new ArrayList<>();

        for (final BaseJson base : baseList) {
            ids.add(base.getId());
        }

        return ids;
    }
}
