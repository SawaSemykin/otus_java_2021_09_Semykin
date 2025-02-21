package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.reflection.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final Map<Class<?>, Object> appComponentsByClass = new HashMap<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();
    private final Object appConfig;

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        appConfig = ReflectionHelper.instantiate(initialConfigClass);
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        Arrays.stream(configClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()))
                .forEachOrdered(this::processConfigMethod);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private void processConfigMethod(Method method) {
        Object[] dependencies = Arrays.stream(method.getParameterTypes())
                .map(this::getAppComponent)
                .toArray();
        Object component = ReflectionHelper.callMethod(appConfig, method, dependencies);
        appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), component);
        appComponentsByClass.put(component.getClass(), component);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        if (componentClass.isInterface()) {
            return (C) getAppComponentByInterface(componentClass);
        } else {
            return (C) getAppComponentByClass(componentClass);
        }
    }

    private Object getAppComponentByInterface(Class<?> componentInterface) {
        List<Class<?>> componentCandidates = appComponentsByClass.keySet().stream()
                .filter(k -> Arrays.stream(k.getInterfaces()).anyMatch(i -> i == componentInterface))
                .collect(Collectors.toList());
        if (componentCandidates.isEmpty()) {
            throw new RuntimeException("Component not found");
        }
        if (componentCandidates.size() > 1) {
            throw new RuntimeException("Found several component candidates matched to parameter type. Use more strict query");
        }
        return appComponentsByClass.get(componentCandidates.get(0));
    }

    private Object getAppComponentByClass(Class<?> componentClass) {
        return appComponentsByClass.get(componentClass);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
