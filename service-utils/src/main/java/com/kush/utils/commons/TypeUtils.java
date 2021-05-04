package com.kush.utils.commons;

import static java.lang.String.format;

import java.lang.reflect.Type;

public class TypeUtils {

    private static final String GENERIC_NAME_TEMPLATE = "%s<%s>";

    public static String getGenericTypeName(Type... typesInOrder) {
        Type innerMostType = typesInOrder[typesInOrder.length - 1];
        String innerTypeName = innerMostType.getTypeName();
        for (int i = typesInOrder.length - 2; i >= 0; i--) {
            String outerTypeName = typesInOrder[i].getTypeName();
            innerTypeName = format(GENERIC_NAME_TEMPLATE, outerTypeName, innerTypeName);
        }
        return innerTypeName;
    }
}
