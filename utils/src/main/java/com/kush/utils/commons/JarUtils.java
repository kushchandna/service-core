package com.kush.utils.commons;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public class JarUtils {

    public static File getClassFile(Class<?> klass) {
        String classAsFile = getClassAsFile(klass);
        URL url = klass.getResource("/" + classAsFile);
        return new File(url.getFile());
    }

    // TODO fix this logic
    // file:/D:/repo/git/commons/utils/target/test-classes
    // file:/D:/repo/git/commons/utils/target/test-classes/com/kush/utils/practice/Resources.class
    public static File getParentDirectory(Class<?> klass) {
        File classFile = getClassFile(klass);
        String classAsFile = getClassAsFile(klass);
        String absolutePath = classFile.getAbsolutePath();
        if (absolutePath.endsWith(classAsFile)) {
            absolutePath = absolutePath.substring(0, absolutePath.length() - classAsFile.length());
        }
        return new File(absolutePath);
    }

    public static void addToJar(File parent, File source, JarOutputStream target) throws IOException {
        BufferedInputStream in = null;
        try {
            if (source.isDirectory()) {
                String name = normaliseName(source);
                if (!name.isEmpty()) {
                    if (!name.endsWith("/")) {
                        name += "/";
                    }
                    addNextEntry(parent, source, target, name);
                    target.closeEntry();
                }
                for (File nestedFile : source.listFiles()) {
                    addToJar(parent, nestedFile, target);
                }
                return;
            }

            String name = normaliseName(source);
            addNextEntry(parent, source, target, name);
            FileInputStream fis = new FileInputStream(source);
            in = new BufferedInputStream(fis);
            byte[] buffer = new byte[1024];
            while (true) {
                int count = in.read(buffer);
                if (count == -1) {
                    break;
                }
                target.write(buffer, 0, count);
            }
            target.closeEntry();
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    private static void addNextEntry(File parent, File source, JarOutputStream target, String name) throws IOException {
        name = name.substring(parent.getAbsolutePath().length() + 1);
        JarEntry entry = new JarEntry(name);
        entry.setTime(source.lastModified());
        target.putNextEntry(entry);
    }

    private static String normaliseName(File source) {
        return source.getPath().replace("\\", "/");
    }

    private static String getClassAsFile(Class<?> klass) {
        return klass.getName().replace('.', '/') + ".class";
    }
}
