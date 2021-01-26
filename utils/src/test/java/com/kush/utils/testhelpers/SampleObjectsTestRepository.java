package com.kush.utils.testhelpers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.rules.ExternalResource;

public class SampleObjectsTestRepository extends ExternalResource {

    private final Map<String, SampleObject> objects = new HashMap<>();

    public static void addObjects(SampleObjectsTestRepository repo, SampleObject... objects) {
        Arrays.stream(objects).forEach(repo::add);
    }

    @Override
    protected void before() throws Throwable {
        objects.clear();
    }

    @Override
    protected void after() {
        objects.clear();
    }

    public SampleObject add(SampleObject object) {
        SampleObject oldObject = objects.get(object.getId());
        objects.put(object.getId(), object);
        return oldObject == null ? null : oldObject.clone();
    }

    public SampleObject remove(String id) {
        return objects.remove(id);
    }
}
