package net.scholagest;

import java.lang.reflect.Field;

public class ReflectionUtils {
    public static void setField(Object object, String fieldName, Object value) {
        try {
            Class<?> clazz = object.getClass();

            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException x) {
            x.printStackTrace();
        } catch (IllegalAccessException x) {
            x.printStackTrace();
        }
    }
}
