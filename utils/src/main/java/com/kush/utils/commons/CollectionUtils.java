package com.kush.utils.commons;

import java.util.Collections;
import java.util.List;

public class CollectionUtils {

    public static <T> List<T> nonNull(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }
}
