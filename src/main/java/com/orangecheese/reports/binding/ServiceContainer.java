package com.orangecheese.reports.binding;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public final class ServiceContainer {
    private static final HashMap<Class<?>, Object> services;

    static {
        services = new HashMap<>();
    }

    public static <T> void bind(Class<T> clazz) {
        try {
            Optional<Constructor<?>> optionalConstructor = Arrays.stream(clazz.getDeclaredConstructors())
                    .toList()
                    .stream()
                    .filter(c -> c.isAnnotationPresent(ServiceConstructor.class))
                    .findFirst();

            Constructor<T> constructor;
            if(optionalConstructor.isPresent()) {
                Class<?>[] constructorTypes = optionalConstructor.get().getParameterTypes();
                constructor = clazz.getConstructor(constructorTypes);
            } else {
                constructor = clazz.getConstructor();
            }

            List<Class<?>> requiredServices = Arrays.asList(constructor.getParameterTypes());
            Object[] requiredServiceInstances = new Object[requiredServices.size()];
            for(int i = 0; i < requiredServiceInstances.length; i++)
                requiredServiceInstances[i] = services.get(requiredServices.get(i));

            T instance = constructor.newInstance(requiredServiceInstances);
            services.put(clazz, instance);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T get(Class<T> clazz) {
        try {
            Object serviceObject = services.get(clazz);

            if(!serviceObject.getClass().isAssignableFrom(clazz))
                throw new ClassCastException();

            return clazz.cast(serviceObject);
        } catch(ClassCastException e) {
            throw new RuntimeException(e);
        }
    }
}