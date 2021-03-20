package com.kush.utils.commons;

import java.util.function.Function;

public class ObjectUtils {

    public static <K, V> V nullOrGet(K object, Function<K, V> getter) {
        return object == null ? null : getter.apply(object);
    }
}
