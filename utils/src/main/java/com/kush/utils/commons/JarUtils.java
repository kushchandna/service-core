package com.kush.utils.commons;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public class JarUtils {

    public static void addToJar(File source, JarOutputStream target) throws IOException {
        BufferedInputStream in = null;
        try {
            if (source.isDirectory()) {
                String name = normaliseName(source);
                if (!name.isEmpty()) {
                    if (!name.endsWith("/")) {
                        name += "/";
                    }
                    addNextEntry(source, target, name);
                    target.closeEntry();
                }
                for (File nestedFile : source.listFiles()) {
                    addToJar(nestedFile, target);
                }
                return;
            }

            String name = normaliseName(source);
            addNextEntry(source, target, name);
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

    private static void addNextEntry(File source, JarOutputStream target, String name) throws IOException {
        JarEntry entry = new JarEntry(name);
        entry.setTime(source.lastModified());
        target.putNextEntry(entry);
    }

    private static String normaliseName(File source) {
        return source.getPath().replace("\\", "/");
    }
}
