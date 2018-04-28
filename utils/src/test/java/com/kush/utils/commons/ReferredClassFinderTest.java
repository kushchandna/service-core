package com.kush.utils.commons;

import static java.lang.annotation.ElementType.TYPE;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.base.Predicate;

@RunWith(Parameterized.class)
public class ReferredClassFinderTest {

    @Target({ TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    static @interface TestExportable {
    }

    @TestExportable
    static class Type1 {
    }

    @TestExportable
    static interface Type2 {
    }

    @TestExportable
    static interface Type3 {
    }

    @TestExportable
    static interface GenericType<T> {
    }

    @TestExportable
    static class ExportableException extends Exception {

        private static final long serialVersionUID = 1L;
    }

    static final class FieldReferencesScenario {
        static Type1 field1;
        String field2;
        Type2 field3;
        List<GenericType<Type3>> field4;
    }

    static final class MethodReturnTypeScenario {
        static Type1 method1() {
            return null;
        }

        String method2() {
            return null;
        }

        Type2 method3() {
            return null;
        }

        List<GenericType<Type3>> method4() {
            return null;
        }
    }

    static final class MethodParametersScenario {
        static void method1(Type1 arg1, String arg2) {
        }

        void method2(Type2 arg1, List<GenericType<Type3>> arg2) {
        }
    }

    static final class MethodExceptionScenario {
        void method1() throws IllegalArgumentException {
        }

        static void method2() throws ExportableException {
        }

        void method3() throws IllegalStateException {
        }
    }

    @TestExportable
    static interface BaseClassLevel1 extends GenericType<Type3> {
        Type1 method1() throws ExportableException;
    }

    @TestExportable
    static abstract class BaseClassLevel2 implements BaseClassLevel1 {
        Type2 arg2;
    }

    static abstract class SuperClassScenario extends BaseClassLevel2 {
    }

    @Parameters
    public static Object[][] getParameters() {
        return new Object[][] {
                { FieldReferencesScenario.class, asList(Type1.class, Type2.class, Type3.class, GenericType.class) },
                { MethodReturnTypeScenario.class, asList(Type1.class, Type2.class, Type3.class, GenericType.class) },
                { MethodParametersScenario.class, asList(Type1.class, Type2.class, Type3.class, GenericType.class) },
                { MethodExceptionScenario.class, asList(ExportableException.class) },
                { SuperClassScenario.class,
                        asList(ExportableException.class, Type1.class, Type2.class, BaseClassLevel1.class,
                                BaseClassLevel2.class, Type3.class, GenericType.class) },
        };
    }

    private final Class<?> testClass;
    private final Class<?>[] expectedReferedTypes;

    public ReferredClassFinderTest(Class<?> testClass, List<Class<?>> expectedReferedTypes) {
        this.testClass = testClass;
        this.expectedReferedTypes = expectedReferedTypes.toArray(new Class<?>[0]);
    }

    @Test
    public void runTest() throws Exception {
        Predicate<Class<?>> isExportable = klass -> klass != null && klass.isAnnotationPresent(TestExportable.class);
        ReferedClassesFinder referedClassesFinder = new ReferedClassesFinder(isExportable);
        Set<Class<?>> referedClasses = referedClassesFinder.find(testClass);
        assertThat(testClass.getName() + " scenario failed.", referedClasses, containsInAnyOrder(expectedReferedTypes));
    }
}
