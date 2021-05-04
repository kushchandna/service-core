package com.kush.utils.commons;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import org.apache.commons.io.IOUtils;

public class JarUtils {

    public static void copyClassFile(Class<?> klass, File targetDirectory) throws IOException {
        String classAsFile = getClassAsFile(klass);
        InputStream inputStream = klass.getResourceAsStream("/" + classAsFile);
        File targetFile = new File(targetDirectory, classAsFile);
        targetFile.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(targetFile);
        IOUtils.copy(inputStream, fos);
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
