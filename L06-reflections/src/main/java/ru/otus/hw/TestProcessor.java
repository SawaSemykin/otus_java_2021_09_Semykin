package ru.otus.hw;

import ru.otus.reflection.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Aleksandr Semykin
 */
public class TestProcessor {

    public void process(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);

        TestContext context = getContext(clazz);

        List<TestResult> results = callTests(context);

        printReport(results, context);
    }

    private TestContext getContext(Class<?> clazz) {
        List<Method> beforeMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();

        for (Method method : clazz.getMethods()) {
            Optional.ofNullable(method.getAnnotation(Before.class)).ifPresent(a -> beforeMethods.add(method));
            Optional.ofNullable(method.getAnnotation(Test.class)).ifPresent(a -> testMethods.add(method));
            Optional.ofNullable(method.getAnnotation(After.class)).ifPresent(a -> afterMethods.add(method));
        }

        return new TestContext(clazz, beforeMethods, testMethods, afterMethods);
    }

    private List<TestResult> callTests(TestContext context) {
        List<TestResult> results = new ArrayList<>();
        for (Method testMethod : context.getTestMethods()) {
            TestResult testResult = callTest(testMethod, context);
            results.add(testResult);
        }
        return results;
    }

    private TestResult callTest(Method testMethod, TestContext context) {
        Object newInstance = ReflectionHelper.instantiate(context.getClazz());

        try {
            callBeforeMethods(newInstance, context);
            return callTestAndGetTestResult(newInstance, testMethod);
        } catch (Exception e) {
            return new TestResult(testMethod.getName(), true, e.getMessage());
        } finally {
            callAfterMethods(newInstance, context);
        }
    }

    private void callBeforeMethods(Object instance, TestContext context) {
        for (Method beforeMethod : context.getBeforeMethods()) {
            ReflectionHelper.callMethod(instance, beforeMethod);
        }
    }

    private void callAfterMethods(Object instance, TestContext context) {
        for (Method afterMethod : context.getAfterMethods()) {
            try {
                ReflectionHelper.callMethod(instance, afterMethod);
            } catch (Exception e) {
                // ignore to complete all the process
            }
        }
    }

    private TestResult callTestAndGetTestResult(Object instance, Method method) {
        TestResult result = new TestResult();
        result.setMethodName(method.getName());
        try {
            ReflectionHelper.callMethod(instance, method);
            return result;
        } catch (Exception e) {
            result.setFailed(true);
            result.setFailedReason(e.getCause().getCause().getMessage());
            return result;
        }
    }

    private void printReport(List<TestResult> results, TestContext context) {
        List<TestResult> failedResults = results.stream().filter(r -> r.isFailed()).collect(Collectors.toList());

        System.out.println("----- " + context.getClazz().getName() + " test stat -----");
        System.out.println(new StringBuilder()
                .append("Passed: ").append(results.size() - failedResults.size())
                .append(", ")
                .append("failed: ").append(failedResults.size())
                .append(", ")
                .append("total: ").append(results.size())
                .append(".")
        );
        System.out.println("   -- failed details --   ");
        for (TestResult result : failedResults) {
            System.out.println(result.getMethodName() + " -> " + result.getFailedReason());
        }
        System.out.println("----- " + context.getClazz().getName() + " test stat -----");
    }
}
