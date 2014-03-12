package net.scholagest.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.scholagest.object.Base;

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

    public static List<String> extractIds(final List<? extends Base> baseList) {
        final List<String> ids = new ArrayList<>();

        for (final Base base : baseList) {
            ids.add(base.getId());
        }

        return ids;
    }
}
