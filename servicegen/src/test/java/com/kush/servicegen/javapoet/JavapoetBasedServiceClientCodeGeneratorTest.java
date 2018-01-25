package com.kush.servicegen.javapoet;

import static com.kush.servicegen.utils.GenericUtilsForTest.assertGenericMethodReturnType;
import static com.kush.servicegen.utils.GenericUtilsForTest.assertGenericParameterType;
import static com.kush.utils.commons.GenericsUtils.getGenericTypeName;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
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

import com.kush.lib.service.client.api.Response;
import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.remoting.api.ServiceApi;
import com.kush.lib.service.server.api.BaseService;
import com.kush.servicegen.CodeGenerator;
import com.kush.servicegen.ServiceInfo;
import com.kush.servicegen.ServiceReader;

public class JavapoetBasedServiceClientCodeGeneratorTest {

    private static final String TARGET_PACKAGE_CLIENT = "com.kush.service.client.gen";
    private static final String GENERATED_SERVICE_CLIENT_NAME = "DummyServiceClient";

    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder();

    private static File temp;
    private static Class<?> serviceClientClass;

    @BeforeClass
    public static void setup() throws Exception {
        temp = folder.getRoot();
        JavaFileObject generatedFileObject = generateDummyClientClass();
        System.out.println(generatedFileObject.getCharContent(true));
        compileGeneratedFile(generatedFileObject);
        loadCompiledClass();
    }

    @Test
    public void serivceClientType() throws Exception {
        String expectedSuperClassTypeName = getGenericTypeName(ServiceClient.class, DummyServiceApi.class);
        Type genericSuperclass = serviceClientClass.getGenericSuperclass();
        assertThat(genericSuperclass.getTypeName(), is(equalTo(expectedSuperClassTypeName)));
    }

    @Test
    public void voidMethodWithNoParams() throws Exception {
        Method method = serviceClientClass.getMethod("aVoidMethodWithNoParams");
        assertGenericMethodReturnType(method, Response.class, Void.class);
    }

    @Test
    public void intMethodWithTwoPrimitiveParams() throws Exception {
        Method method = serviceClientClass.getMethod("bIntMethodWithTwoPrimitiveParams", int.class, double.class);
        assertGenericMethodReturnType(method, Response.class, int.class);
    }

    @Test
    public void stringMethodWithTwoNonPrimitiveParams() throws Exception {
        Method method = serviceClientClass.getMethod("cStringMethodWithTwoNonPrimitiveParams", Integer.class, Double.class);
        assertGenericMethodReturnType(method, Response.class, String.class);
    }

    @Test
    public void intArrayMethodWithTwoArrayParams() throws Exception {
        Method method = serviceClientClass.getMethod("dIntArrayMethodWithTwoArrayParams", int[].class, Double[].class);
        assertGenericMethodReturnType(method, Response.class, int[].class);
    }

    @Test
    public void stringListMethodWithTwoGenericParams() throws Exception {
        Method method = serviceClientClass.getMethod("eStringListMethodWithTwoGenericParams", List.class, Set.class);
        Parameter[] parameters = method.getParameters();
        assertGenericParameterType(parameters[0], List.class, Integer.class);
        assertGenericParameterType(parameters[1], Set.class, Double.class);
        assertGenericMethodReturnType(method, Response.class, List.class, String.class);
    }

    private static void compileGeneratedFile(JavaFileObject generatedFileObject) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> options = asList("-d", temp.getAbsolutePath());
        CompilationTask task =
                compiler.getTask(null, null, null, options, asList(DummyServiceApi.class.getName()), asList(generatedFileObject));
        task.call();
    }

    private static void loadCompiledClass() throws Exception {
        URL generatedFileUrl = temp.toURI().toURL();
        try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { generatedFileUrl })) {
            serviceClientClass = urlClassLoader.loadClass(TARGET_PACKAGE_CLIENT + "." + GENERATED_SERVICE_CLIENT_NAME);
        }
    }

    private static JavaFileObject generateDummyClientClass() throws Exception {
        ServiceReader serviceReader = new ServiceReader();
        ServiceInfo serviceInfo = serviceReader.readService(DummyService.class);
        CodeGenerator generator = new JavapoetBasedServiceClientCodeGenerator(serviceInfo, DummyServiceApi.class);
        return generator.generate(TARGET_PACKAGE_CLIENT, temp);
    }

    public static interface DummyServiceApi extends ServiceApi {

        void aVoidMethodWithNoParams();

        int bIntMethodWithTwoPrimitiveParams(int param1, double param2);

        String cStringMethodWithTwoNonPrimitiveParams(Integer param1, Double param2);

        int[] dIntArrayMethodWithTwoArrayParams(int[] param1, Double[] param2);

        List<String> eStringListMethodWithTwoGenericParams(List<Integer> param1, Set<Double> param2);
    }

    public static class DummyService extends BaseService {

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
