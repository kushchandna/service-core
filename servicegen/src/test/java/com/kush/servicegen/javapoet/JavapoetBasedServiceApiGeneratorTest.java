package com.kush.servicegen.javapoet;

import static com.kush.utils.commons.GenericsUtils.getGenericReturnTypeName;
import static com.kush.utils.commons.GenericsUtils.getGenericTypeName;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Set;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.kush.lib.service.remoting.api.ServiceApi;
import com.kush.servicegen.api.ServiceApiGenerator;
import com.kush.servicegen.api.ServiceReader;
import com.kush.servicegen.defaults.DefaultServiceReader;

public class JavapoetBasedServiceApiGeneratorTest {

    private static final String TARGET_PACKAGE = "com.kush.service.api.gen";
    private static final String GENERATED_SERVICE_API_NAME = "DummyServiceApi";

    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder();

    private static File temp;
    private static Class<?> serviceApiClass;

    @BeforeClass
    public static void setup() throws Exception {
        temp = folder.getRoot();
        ServiceReader serviceReader = new DefaultServiceReader();
        ServiceApiGenerator generator = new JavapoetBasedServiceApiGenerator(serviceReader);
        JavaFileObject generatedFileObject = generator.generateServiceApiClass(DummyService.class, TARGET_PACKAGE, temp);
        System.out.println(generatedFileObject.getCharContent(true));
        compileGeneratedFile(generatedFileObject);
        loadCompiledClass();
    }

    @Test
    public void serivceApiType() throws Exception {
        assertThat(ServiceApi.class.isAssignableFrom(serviceApiClass), is(true));
    }

    @Test
    public void voidMethodWithNoParams() throws Exception {
        Method method = serviceApiClass.getMethod("aVoidMethodWithNoParams");
        assertThat(method.getReturnType(), is(equalTo(void.class)));
    }

    @Test
    public void intMethodWithTwoPrimitiveParams() throws Exception {
        Method method = serviceApiClass.getMethod("bIntMethodWithTwoPrimitiveParams", int.class, double.class);
        assertThat(method.getReturnType(), is(equalTo(int.class)));
    }

    @Test
    public void stringMethodWithTwoNonPrimitiveParams() throws Exception {
        Method method = serviceApiClass.getMethod("cStringMethodWithTwoNonPrimitiveParams", Integer.class, Double.class);
        assertThat(method.getReturnType(), is(equalTo(String.class)));
    }

    @Test
    public void intArrayMethodWithTwoArrayParams() throws Exception {
        Method method = serviceApiClass.getMethod("dIntArrayMethodWithTwoArrayParams", int[].class, Double[].class);
        assertThat(method.getReturnType(), is(equalTo(int[].class)));
    }

    @Test
    public void stringListMethodWithTwoGenericParams() throws Exception {
        Method method = serviceApiClass.getMethod("eStringListMethodWithTwoGenericParams", List.class, Set.class);
        Parameter[] parameters = method.getParameters();
        assertThat(getGenericTypeName(parameters[0]), is(equalTo(getGenericTypeName(List.class, Integer.class))));
        assertThat(getGenericTypeName(parameters[1]), is(equalTo(getGenericTypeName(Set.class, Double.class))));
        assertThat(getGenericReturnTypeName(method), is(equalTo(getGenericTypeName(List.class, String.class))));
    }

    private static void compileGeneratedFile(JavaFileObject generatedFileObject) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> options = asList("-d", temp.getAbsolutePath());
        CompilationTask task = compiler.getTask(null, null, null, options, null, asList(generatedFileObject));
        task.call();
    }

    private static void loadCompiledClass() throws Exception {
        URL generatedFileUrl = temp.toURI().toURL();
        try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { generatedFileUrl })) {
            serviceApiClass = urlClassLoader.loadClass(TARGET_PACKAGE + "." + GENERATED_SERVICE_API_NAME);
        }
    }

    static class DummyService {

        public void aVoidMethodWithNoParams() {
        }

        public int bIntMethodWithTwoPrimitiveParams(int param1, double param2) {
            return 0;
        }

        public String cStringMethodWithTwoNonPrimitiveParams(Integer param1, Double param2) {
            return null;
        }

        public int[] dIntArrayMethodWithTwoArrayParams(int[] param1, Double[] param2) {
            return null;
        }

        public List<String> eStringListMethodWithTwoGenericParams(List<Integer> param1, Set<Double> param2) {
            return null;
        }
    }
}
