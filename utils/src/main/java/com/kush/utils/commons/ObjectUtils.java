package com.kush.utils.commons;

import java.util.Comparator;
import java.util.function.Function;

public class ObjectUtils {

    public static <K, V> V nullOrGet(K object, Function<K, V> getter) {
        return object == null ? null : getter.apply(object);
    }

    public static <T> T max(T o1, T o2, Comparator<T> comparator) {
        int comparision = comparator.compare(o1, o2);
        return comparision <= 0 ? o1 : o2;
    }

    public static <T> T min(T o1, T o2, Comparator<T> comparator) {
        int comparision = comparator.compare(o1, o2);
        return comparision >= 0 ? o1 : o2;
    }
}
