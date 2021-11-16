package ru.otus.homework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Aleksandr Semykin
 */
class Ioc {
    private Ioc() {}

    public static TestLogging createInstance() {
        InvocationHandler handler = new LoggerInvocationHandler(new TestLoggingImpl());
        return (TestLogging) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[] {TestLogging.class}, handler);

    }

    static class LoggerInvocationHandler implements InvocationHandler {
        private final TestLogging srcInstance;
        private final Set<Method> loggedMethods;

        LoggerInvocationHandler(TestLogging srcInstance) {
            this.srcInstance = srcInstance;
            loggedMethods = Arrays.stream(TestLogging.class.getDeclaredMethods())
                    .filter(this::isAnnotationPresent)
                    .collect(Collectors.toSet());
        }

        private boolean isAnnotationPresent(Method method) {
            try {
                Method methodImpl = srcInstance.getClass().getMethod(method.getName(), method.getParameterTypes());
                return methodImpl.isAnnotationPresent(Log.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            log(method, args);
            return method.invoke(srcInstance, args);
        }

        private void log(Method method, Object[] args) {
            if (loggedMethods.contains(method)) {
                System.out.print("executed method: " + method);
                System.out.println(", params: " + Arrays.toString(args));

            }
        }
    }
}
