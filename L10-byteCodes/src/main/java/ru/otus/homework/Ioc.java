package ru.otus.homework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
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

        LoggerInvocationHandler(TestLogging srcInstance) {
            this.srcInstance = srcInstance;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            log(method, args);
            return method.invoke(srcInstance, args);
        }

        private void log(Method method, Object[] args) throws Throwable {
            Method methodImpl = srcInstance.getClass().getMethod(method.getName(), method.getParameterTypes());
            if (methodImpl.getAnnotation((Log.class) ) != null) {
                System.out.print("executed method: " + method.getName());
                System.out.println(", params: " + Arrays.stream(args).map(this::parseParam).collect(Collectors.joining(", ")));
            }
        }

        private String parseParam(Object param) {
            return System.getProperty("line.separator") +
                    "[type: " + param.getClass() +
                    ", value: " + param + "]";
        }
    }
}
