package com.kush.servicegen.javapoet;

import static com.kush.servicegen.utils.GenericUtilsForTest.assertGenericMethodReturnType;
import static com.kush.servicegen.utils.GenericUtilsForTest.assertGenericParameterType;
import static com.kush.servicegen.utils.GenericUtilsForTest.assertMethodReturnType;
import static java.util.Arrays.asList;
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
import com.kush.servicegen.CodeGenerator;
import com.kush.servicegen.ServiceInfo;
import com.kush.servicegen.ServiceReader;

public class JavapoetBasedServiceApiCodeGeneratorTest {

    private static final String TARGET_PACKAGE = "com.kush.service.api.gen";
    private static final String GENERATED_SERVICE_API_NAME = "DummyServiceApi";

    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder();

    private static File temp;
    private static Class<?> serviceApiClass;

    @BeforeClass
    public static void setup() throws Exception {
        temp = folder.getRoot();
        JavaFileObject generatedFileObject = generateDummyServiceApi();
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
        assertMethodReturnType(method, void.class);
    }

    @Test
    public void intMethodWithTwoPrimitiveParams() throws Exception {
        Method method = serviceApiClass.getMethod("bIntMethodWithTwoPrimitiveParams", int.class, double.class);
        assertMethodReturnType(method, int.class);
    }

    @Test
    public void stringMethodWithTwoNonPrimitiveParams() throws Exception {
        Method method = serviceApiClass.getMethod("cStringMethodWithTwoNonPrimitiveParams", Integer.class, Double.class);
        assertMethodReturnType(method, String.class);
    }

    @Test
    public void intArrayMethodWithTwoArrayParams() throws Exception {
        Method method = serviceApiClass.getMethod("dIntArrayMethodWithTwoArrayParams", int[].class, Double[].class);
        assertMethodReturnType(method, int[].class);
    }

    @Test
    public void stringListMethodWithTwoGenericParams() throws Exception {
        Method method = serviceApiClass.getMethod("eStringListMethodWithTwoGenericParams", List.class, Set.class);
        Parameter[] parameters = method.getParameters();
        assertGenericParameterType(parameters[0], List.class, Integer.class);
        assertGenericParameterType(parameters[1], Set.class, Double.class);
        assertGenericMethodReturnType(method, List.class, String.class);
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

    private static JavaFileObject generateDummyServiceApi() throws Exception {
        ServiceReader serviceReader = new ServiceReader();
        ServiceInfo serviceInfo = serviceReader.readService(DummyService.class);
        CodeGenerator generator = new JavapoetBasedServiceApiCodeGenerator(serviceInfo);
        return generator.generate(TARGET_PACKAGE, temp);
    }
}
