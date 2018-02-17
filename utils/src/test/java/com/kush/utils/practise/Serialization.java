package com.kush.utils.practise;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class Serialization {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Test(expected = InvalidClassException.class)
    public void noValidConstructor() throws Exception {
        writeReadPrint(new SerializableWithNoEmptyConstructorA("test"));
    }

    @Test(expected = InvalidClassException.class)
    public void noExplicitConstructorInSuperClass() throws Exception {
        writeReadPrint(new SerializableWithNoEmptyConstructorB("test"));
    }

    @Test(expected = InvalidClassException.class)
    public void noValidConstructorEvenWithEmptyOneInChildClass() throws Exception {
        writeReadPrint(new SerializableWithEmptyConstructor());
    }

    @Test
    public void explicitEmptyConstructorInSuperClass() throws Exception {
        writeReadPrint(new SerializableWithNoExplicitEmptyConstructor());
    }

    private void writeReadPrint(Object data) throws FileNotFoundException, IOException, ClassNotFoundException {
        File file = new File(temp.getRoot(), "testfile.dat");
        OutputStream os = new FileOutputStream(file);
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(data);
        }
        InputStream is = new FileInputStream(file);
        Object object;
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            object = ois.readObject();
        }
        System.out.println(object);
    }

    private static class SerializableWithNoEmptyConstructorA extends NonSerializableWithNoEmptyConstructor
            implements Serializable {

        private static final long serialVersionUID = 1L;

        public SerializableWithNoEmptyConstructorA(String name) {
            super(name);
        }
    }

    private static class SerializableWithEmptyConstructor extends NonSerializableWithNoEmptyConstructor implements Serializable {

        private static final long serialVersionUID = 1L;

        public SerializableWithEmptyConstructor() {
            super("test");
        }
    }

    private static class SerializableWithNoEmptyConstructorB extends NonSerializableWithNoExplicitEmptyConstructor
            implements Serializable {

        private static final long serialVersionUID = 1L;

        public SerializableWithNoEmptyConstructorB(String name) {
        }
    }

    private static class SerializableWithNoExplicitEmptyConstructor extends NonSerializableWithExplicitEmptyConstructor
            implements Serializable {

        private static final long serialVersionUID = 1L;
    }

    private static class NonSerializableWithNoEmptyConstructor {

        public NonSerializableWithNoEmptyConstructor(String name) {
        }
    }

    private static class NonSerializableWithNoExplicitEmptyConstructor {
    }

    private static class NonSerializableWithExplicitEmptyConstructor {

        public NonSerializableWithExplicitEmptyConstructor() {
        }
    }
}
