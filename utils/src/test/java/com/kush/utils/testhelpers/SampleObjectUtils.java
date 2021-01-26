package com.kush.utils.testhelpers;

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
}
