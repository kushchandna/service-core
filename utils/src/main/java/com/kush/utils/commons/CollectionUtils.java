package com.kush.utils.commons;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtils {

    public static <T> List<T> nonNull(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }

    public static <T> Set<T> nonNull(Set<T> set) {
        return set == null ? Collections.emptySet() : set;
    }

    public static <K, V> Map<K, Set<V>> singletonMultiValueMap(K key, V value) {
        return singletonMap(key, singleton(value));
    }
}
