package com.xpping.windows10.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by xlzhen on 9/8 0008.
 * 反射调用 工具
 */

public class ReflectUtils {
    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }

        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Field getAccessibleField(final Object obj, final String fieldName) {
        if (obj == null) {
            throw new IllegalArgumentException("object can't be null");
        }

        if (fieldName == null || fieldName.length() <= 0) {
            throw new IllegalArgumentException("fieldName can't be blank");
        }

        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            } catch (NoSuchFieldException e) {
                continue;
            }
        }
        return null;
    }

    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }
}
