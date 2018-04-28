package com.kush.utils.commons;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Predicate;

public class ReferedClassesFinder {

    private final Predicate<Class<?>> filter;

    public ReferedClassesFinder() {
        this(c -> true);
    }

    public ReferedClassesFinder(Predicate<Class<?>> filter) {
        this.filter = filter;
    }

    public Set<Class<?>> find(Class<?> klass) {
        Set<Class<?>> klasses = new HashSet<>();
        findAndPopulate(klass, klasses);
        return klasses;
    }

    private void findAndPopulate(Class<?> klass, Set<Class<?>> klasses) {
        processFields(klass, klasses);
        processMethods(klass, klasses);
        processSuperClasses(klass, klasses);
    }

    private void processSuperClasses(Class<?> klass, Set<Class<?>> klasses) {
        processSuperClass(klass, klasses);
        processAsGeneric(klasses, klass.getGenericSuperclass());
        processSuperInterfaces(klass, klasses);
        Type[] genericInterfaces = klass.getGenericInterfaces();
        for (Type genericSuperInterface : genericInterfaces) {
            processAsGeneric(klasses, genericSuperInterface);
        }
    }

    private void processMethods(Class<?> klass, Set<Class<?>> klasses) {
        Method[] declaredMethods = klass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            processReturnType(klasses, method);
            processParameters(klasses, method);
            processExceptions(klasses, method);
        }
    }

    private void processExceptions(Set<Class<?>> klasses, Method method) {
        Class<?>[] exceptionTypes = method.getExceptionTypes();
        for (Class<?> exceptionType : exceptionTypes) {
            addToClasses(klasses, exceptionType);
        }
    }

    private void processParameters(Set<Class<?>> klasses, Method method) {
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            addToClasses(klasses, parameter.getType());
            processAsGeneric(klasses, parameter.getParameterizedType());
        }
    }

    private void processReturnType(Set<Class<?>> klasses, Method method) {
        addToClasses(klasses, method.getReturnType());
        processAsGeneric(klasses, method.getGenericReturnType());
    }

    private void processFields(Class<?> klass, Set<Class<?>> klasses) {
        Field[] declaredFields = klass.getDeclaredFields();
        for (Field field : declaredFields) {
            addToClasses(klasses, field.getType());
            processAsGeneric(klasses, field.getGenericType());
        }
    }

    private void processSuperInterfaces(Class<?> klass, Set<Class<?>> klasses) {
        Class<?>[] interfaces = klass.getInterfaces();
        for (Class<?> interfaceType : interfaces) {
            addToClasses(klasses, interfaceType);
        }
    }

    private void processSuperClass(Class<?> klass, Set<Class<?>> klasses) {
        Class<?> superclass = klass.getSuperclass();
        addToClasses(klasses, superclass);
    }

    private void processAsGeneric(Set<Class<?>> klasses, Type genericType) {
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            addToClasses(klasses, (Class<?>) parameterizedType.getRawType());
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            for (Type actualType : actualTypeArguments) {
                processAsGeneric(klasses, actualType);
            }
        } else if (genericType instanceof Class<?>) {
            addToClasses(klasses, (Class<?>) genericType);
        }
    }

    private void addToClasses(Set<Class<?>> klasses, Class<?> type) {
        if (type != null && !klasses.contains(type) && filter.apply(type)) {
            klasses.add(type);
            findAndPopulate(type, klasses);
        }
    }
}
