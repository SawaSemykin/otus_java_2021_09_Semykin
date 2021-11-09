package ru.otus.hw;

import lombok.Value;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Aleksandr Semykin
 */
@Value
public class TestContext {
    Class<?> clazz;
    List<Method> beforeMethods;
    List<Method> testMethods;
    List<Method> afterMethods;
}
