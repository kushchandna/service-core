package com.kush.utils.commons;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CollectionUtils {

    public static <T> List<T> nonNull(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }

    public static <T> Set<T> nonNull(Set<T> set) {
        return set == null ? Collections.emptySet() : set;
    }
}
