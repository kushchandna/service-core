package com.kush.utils.commons;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;


@Ignore
public class JarUtilsTest {

    @Test
    public void testName() throws Exception {
        String classAsFile = getClassAsFile(String.class);
        System.out.println(classAsFile);

        InputStream stream = String.class.getResourceAsStream("/" + classAsFile);
        String string = IOUtils.toString(stream, "UTF-8");
        System.out.println(string);
    }

    private static String getClassAsFile(Class<?> klass) {
        return klass.getName().replace('.', '/') + ".class";
    }
}
