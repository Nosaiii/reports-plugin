package com.orangecheese.reports.utility;

import java.lang.reflect.Field;

public final class ReflectionUtility {
    public static void setFieldValue(Object obj, String fieldName, Object value) {
        try {
            Field field = getField(obj.getClass(), fieldName);
            field.set(null, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getFieldValue(Object obj, String fieldName) {
        try {
            Field field = getField(obj.getClass(), fieldName);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> Field getField(Class<T> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}