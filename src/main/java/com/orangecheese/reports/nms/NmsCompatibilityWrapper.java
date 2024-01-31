package com.orangecheese.reports.nms;

import com.orangecheese.reports.utility.ReflectionUtility;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class NmsCompatibilityWrapper {
    private final Object object;

    private final Class<?> clazz;

    private NmsCompatibilityWrapper(Object object, Class<?> clazz) {
        this.object = object;
        this.clazz = clazz;
    }

    public static <T> NmsCompatibilityWrapper asWrap(Object object, Class<T> wrappedClass) {
        return new NmsCompatibilityWrapper(object, wrappedClass);
    }

    public static NmsCompatibilityWrapper asCast(String className, String packagePrefix, Object object) {
        try {
            Class<?> clazz = Class.forName(getClassPathWithPackagePrefix(className, packagePrefix));
            return asWrap(clazz.cast(object), clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static NmsCompatibilityWrapper asCast(String className, Object object) {
        return asCast(className, null, object);
    }

    public static <T> T staticGetMethod(String className, String packagePrefix, String methodName, Class<T> returnType, Object[] parameters) {
        if(parameters == null)
            parameters = new Object[0];

        try {
            Class<?> staticClass = Class.forName(getClassPathWithPackagePrefix(className, packagePrefix));
            Method method = staticClass.getMethod(methodName, getParameterTypesByParameters(parameters));
            Object returnedObject = method.invoke(null, parameters);
            return returnType.cast(returnedObject);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T staticGetMethod(String className, String methodName, Class<T> returnType, Object[] parameters) {
        return staticGetMethod(className, null, methodName, returnType, parameters);
    }

    public static <T> T staticGetMethod(String className, String packagePrefix, String methodName, Class<T> returnType) {
        return staticGetMethod(className, packagePrefix, methodName, returnType, null);
    }

    public static <T> T staticGetMethod(String className, String methodName, Class<T> returnType) {
        return staticGetMethod(className, null, methodName, returnType, null);
    }

    public <T> T getMethod(String methodName, Object[] parameters, Class<T> returnType) {
        try {
            Method method = clazz.getMethod(methodName, getParameterTypesByParameters(parameters));
            Object returnedObject = method.invoke(object, parameters);
            return returnType.cast(returnedObject);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getMethod(String methodName, Class<T> returnType) {
        return getMethod(methodName, null, returnType);
    }

    public <T> T getField(String fieldName, Class<T> returnType) {
        Object fieldValue = ReflectionUtility.getFieldValue(object, fieldName);

        if(!fieldValue.getClass().isAssignableFrom(returnType))
            throw new ClassCastException();

        return returnType.cast(fieldValue);
    }

    private static String getClassPathWithPackagePrefix(String className, String packagePrefix) {
        StringBuilder classPathBuilder = new StringBuilder();
        classPathBuilder.append("org.bukkit.craftbukkit.");
        classPathBuilder.append(getOBCVersion());
        classPathBuilder.append(".");
        if(packagePrefix != null && !packagePrefix.isEmpty())
            classPathBuilder.append(packagePrefix).append(".");
        classPathBuilder.append(className);

        return classPathBuilder.toString();
    }

    private static Class<?>[] getParameterTypesByParameters(Object[] parameters) {
        Class<?>[] methodParameterTypes = new Class[0];
        if(parameters != null && parameters.length > 0)
            methodParameterTypes = Arrays.stream(parameters)
                    .map(Object::getClass)
                    .toArray(Class[]::new);

        return methodParameterTypes;
    }

    private static String getOBCVersion() {
        String obcServerPackageName = Bukkit
                .getServer()
                .getClass()
                .getPackage()
                .getName();
        return obcServerPackageName.substring(obcServerPackageName.lastIndexOf(".") + 1);
    }
}