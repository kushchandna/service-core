package com.kush.utils.testhelpers;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class SampleObjectUtils {

    public static SampleObject obj(String id) {
        SampleObject object = new SampleObject();
        object.setId(id);
        return object;
    }

    public static SampleObject obj(String id, String name) {
        SampleObject object = obj(id);
        object.setName(name);
        return object;
    }

    public static String id(String id) {
        return id;
    }

    public static String name(String name) {
        return name;
    }

    public static Stream<SampleObject> generate() {
        AtomicLong idCounter = new AtomicLong(0);
        return Stream.generate(() -> {
            long id = idCounter.addAndGet(1L);
            return obj("id" + id, "name" + id);
        });
    }
}
