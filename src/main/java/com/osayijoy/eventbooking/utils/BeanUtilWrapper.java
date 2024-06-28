package com.osayijoy.eventbooking.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.BeanUtils;
/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */

public final class BeanUtilWrapper {
    public static void copyNonNullProperties(Object source, Object target) {
        String[] nullFields = getNullFields(source);
        BeanUtils.copyProperties(source, target, nullFields);
    }

    private static String[] getNullFields(Object source) {
        Field[] declaredFields = source.getClass().getDeclaredFields();
        List<String> emptyFieldNames = Arrays.stream(declaredFields).filter((field) -> {
            field.setAccessible(true);

            Object o;
            try {
                o = field.get(source);
            } catch (IllegalAccessException var4) {
                throw new RuntimeException(var4.getMessage(), var4);
            }

            return Objects.isNull(o);
        }).map(Field::getName).toList();
        String[] emptyFieldsArray = new String[emptyFieldNames.size()];
        return (String[])emptyFieldNames.toArray(emptyFieldsArray);
    }

    private BeanUtilWrapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

